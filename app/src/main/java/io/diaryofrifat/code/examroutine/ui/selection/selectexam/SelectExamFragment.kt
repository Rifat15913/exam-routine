package io.diaryofrifat.code.examroutine.ui.selection.selectexam

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.callback.ItemClickListener
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.base.helper.GridSpacingItemDecoration
import io.diaryofrifat.code.examroutine.ui.base.makeItGone
import io.diaryofrifat.code.examroutine.ui.base.makeItVisible
import io.diaryofrifat.code.examroutine.ui.home.container.HomeActivity
import io.diaryofrifat.code.examroutine.ui.selection.container.SelectionContainerActivity
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_select_exam.*
import timber.log.Timber


class SelectExamFragment : BaseFragment<SelectExamMvpView, SelectExamPresenter>(), SelectExamMvpView {

    private var mSelectedExamType: ExamType? = null
    private var mItemDecoration =
            GridSpacingItemDecoration(2, ViewUtils.getPixel(R.dimen.margin_8))

    override val layoutId: Int
        get() = R.layout.fragment_select_exam

    override fun getFragmentPresenter(): SelectExamPresenter {
        return SelectExamPresenter()
    }

    override fun startUI() {
        initialize()
        setListeners()
        loadData()
    }

    private fun initialize() {
        ViewUtils.initializeRecyclerView(
                recycler_view_exams,
                SelectExamAdapter(),
                object : ItemClickListener<ExamType> {
                    override fun onItemClick(view: View, item: ExamType, position: Int) {
                        super.onItemClick(view, item, position)
                        clickOnItem(item)
                    }
                },
                null,
                GridLayoutManager(mContext, 2),
                mItemDecoration,
                null,
                null)
    }

    override fun onStart() {
        super.onStart()
        loadAd()
        addContinuousListeners()
    }

    private fun addContinuousListeners() {
        presenter.attachExamTypeListener()
        presenter.compositeDisposable.add(getAdapter().dataChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it == 0) {
                        text_view_empty_placeholder?.makeItVisible()
                    } else {
                        text_view_empty_placeholder?.makeItGone()
                    }
                }, {
                    Timber.e(it)
                })
        )
    }

    override fun onStop() {
        super.onStop()
        presenter.detachExamTypeListener()
    }

    private fun setListeners() {

    }

    private fun loadAd() {
        MobileAds.initialize(mContext, DataUtils.getString(R.string.admob_app_id))
        banner_ad_view?.loadAd(AdRequest.Builder().build())
    }

    private fun loadData() {
        presenter.checkInternetConnectivity()
        presenter.getExamTypes()
    }

    override fun stopUI() {

    }

    private fun clickOnItem(item: ExamType) {
        mSelectedExamType = item
        goToNextPage()
    }

    private fun goToNextPage() {
        if (mSelectedExamType != null) {
            presenter.getSubcategories(mSelectedExamType?.examTypeKey!!)
        }
    }

    private fun getAdapter(): SelectExamAdapter {
        return recycler_view_exams?.adapter as SelectExamAdapter
    }

    override fun onGettingExamTypes(list: List<ExamType>) {
        getAdapter().clear()
        getAdapter().addItems(list)
    }

    override fun onError(error: Throwable) {
        Timber.e(error)
        ToastUtils.nativeShort(getString(R.string.something_went_wrong))
    }

    override fun onInternetConnectivity(isConnected: Boolean) {
        if (!isConnected) {
            ToastUtils.nativeLong(getString(R.string.error_you_are_not_connected_to_the_internet))
        }
    }

    override fun onGettingSubcategories(subcategoryList: List<ExamType>) {
        if (subcategoryList.isEmpty()) {
            if (mSelectedExamType != null) {
                HomeActivity.startActivity(mContext, mSelectedExamType!!, null)
            }
        } else {
            if (mSelectedExamType != null) {
                (activity as SelectionContainerActivity)
                        .visitSelectSubcategory(subcategoryList, mSelectedExamType!!)
            }
        }
    }
}