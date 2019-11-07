package io.diaryofrifat.code.examroutine.ui.selection.selectexam

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.callback.ItemClickListener
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.base.helper.GridSpacingItemDecoration
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import kotlinx.android.synthetic.main.fragment_select_exam.*
import timber.log.Timber


class SelectExamFragment : BaseFragment<SelectExamMvpView, SelectExamPresenter>(), SelectExamMvpView {

    private var mInterstitialAd: InterstitialAd? = null
    private var mSelectedExamType: ExamType? = null

    override val layoutId: Int
        get() = R.layout.fragment_select_exam

    override fun getFragmentPresenter(): SelectExamPresenter {
        return SelectExamPresenter()
    }

    override fun startUI() {
        initialize()
        setListeners()
        loadAd()
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
                GridSpacingItemDecoration(2,
                        ViewUtils.getPixel(R.dimen.margin_8),
                        true),
                null,
                null)
    }

    private fun setListeners() {
        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                goToSubcategoryPage()
            }
        }
    }

    private fun loadAd() {
        MobileAds.initialize(mContext, DataUtils.getString(R.string.admob_app_id))
        mInterstitialAd = InterstitialAd(mContext)
        mInterstitialAd?.adUnitId = getString(R.string.select_exam_type_ad_unit_id)
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun loadData() {
        presenter.checkInternetConnectivity()
        presenter.getExamTypes(mContext)
    }

    override fun stopUI() {
        presenter.detachExamTypeListener()
        mInterstitialAd?.adListener = null
        mInterstitialAd = null
    }

    private fun clickOnItem(item: ExamType) {
        mSelectedExamType = item

        if (mInterstitialAd?.isLoaded!!) {
            mInterstitialAd?.show()
        } else {
            goToSubcategoryPage()
        }
    }

    private fun goToSubcategoryPage() {
        ToastUtils.nativeLong("Item: $mSelectedExamType")
    }

    private fun getAdapter(): SelectExamAdapter {
        return recycler_view_exams?.adapter as SelectExamAdapter
    }

    override fun onGettingExamTypes(list: List<ExamType>) {
        getAdapter().clear()
        getAdapter().addItems(list)
    }

    override fun onErrorGettingExamTypes(error: Throwable) {
        Timber.e(error)
        ToastUtils.nativeShort(getString(R.string.something_went_wrong))
    }

    override fun onInternetConnectivity(isConnected: Boolean) {
        if (!isConnected) {
            ToastUtils.nativeLong(getString(R.string.error_you_are_not_connected_to_the_internet))
        }
    }
}