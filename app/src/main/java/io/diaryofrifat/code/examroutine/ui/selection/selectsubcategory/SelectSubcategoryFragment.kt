package io.diaryofrifat.code.examroutine.ui.selection.selectsubcategory

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
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_select_subcategory.*
import timber.log.Timber


class SelectSubcategoryFragment
    : BaseFragment<SelectSubcategoryMvpView, SelectSubcategoryPresenter>(),
        SelectSubcategoryMvpView {

    private var mSubcategoryList: MutableList<ExamType> = ArrayList()
    private var mCategory: ExamType? = null
    private var mItemDecoration =
            GridSpacingItemDecoration(2, ViewUtils.getPixel(R.dimen.margin_8))

    override val layoutId: Int
        get() = R.layout.fragment_select_subcategory

    override fun getFragmentPresenter(): SelectSubcategoryPresenter {
        return SelectSubcategoryPresenter()
    }

    override fun startUI() {
        extractDataFromArguments()
        initialize()
        loadAd()
        loadData()
    }

    private fun extractDataFromArguments() {
        val bundle = arguments
        if (bundle != null
                && bundle.containsKey(Constants.IntentKey.CATEGORY)
                && bundle.containsKey(Constants.IntentKey.SUBCATEGORY)
                && bundle.getParcelable<ExamType>(Constants.IntentKey.CATEGORY) != null
                && bundle.getParcelableArrayList<ExamType>(Constants.IntentKey.SUBCATEGORY) != null
        ) {
            mSubcategoryList.clear()
            mSubcategoryList.addAll(bundle.getParcelableArrayList(Constants.IntentKey.SUBCATEGORY)!!)
            mCategory = bundle.getParcelable(Constants.IntentKey.CATEGORY)
        } else {
            (activity as SelectionContainerActivity).onBackPressed()
        }
    }

    private fun initialize() {
        ViewUtils.initializeRecyclerView(
                recycler_view_subcategory,
                SelectSubcategoryAdapter(),
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

    private fun loadAd() {
        MobileAds.initialize(mContext, DataUtils.getString(R.string.admob_app_id))
        banner_ad_view?.loadAd(AdRequest.Builder().build())
    }

    private fun loadData() {
        presenter.checkInternetConnectivity()

        getAdapter().clear()
        getAdapter().addItems(mSubcategoryList)
    }

    override fun stopUI() {

    }

    private fun clickOnItem(item: ExamType) {
        if(mCategory != null) {
            HomeActivity.startActivity(mContext, mCategory!!, item)
        }
    }

    private fun getAdapter(): SelectSubcategoryAdapter {
        return recycler_view_subcategory?.adapter as SelectSubcategoryAdapter
    }

    override fun onInternetConnectivity(isConnected: Boolean) {
        if (!isConnected) {
            ToastUtils.nativeLong(getString(R.string.error_you_are_not_connected_to_the_internet))
        }
    }
}