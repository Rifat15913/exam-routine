package io.diaryofrifat.code.examroutine.ui.routine

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.databinding.FragmentExamRoutineBinding
import io.diaryofrifat.code.examroutine.ui.base.callback.ItemClickListener
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.base.helper.LinearMarginItemDecoration
import io.diaryofrifat.code.examroutine.ui.examdetails.ExamDetailsActivity
import io.diaryofrifat.code.utils.helper.*
import io.diaryofrifat.code.utils.libs.ToastUtils
import timber.log.Timber
import java.util.*

class RoutineFragment : BaseFragment<RoutineMvpView, RoutinePresenter>(), RoutineMvpView {

    private lateinit var mBinding: FragmentExamRoutineBinding
    private lateinit var mMarginItemDecoration: LinearMarginItemDecoration
    private var mInterstitialAd: InterstitialAd? = null
    private var mItem: Exam? = null

    override val layoutId: Int
        get() = R.layout.fragment_exam_routine

    override fun getFragmentPresenter(): RoutinePresenter {
        return RoutinePresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as FragmentExamRoutineBinding

        workWithAds()
        workWithViews()
        if (workWithRoutineData()) {
            activity?.finish()
            return
        }
        setListeners()
        workWithValidation()
    }

    private fun workWithValidation() {
        presenter.checkInternetConnectivity()
    }

    private fun setListeners() {
        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                // Go to next page
                if (mItem != null) {
                    goToExamDetailsPage(mItem!!)
                }
            }
        }
    }

    private fun workWithRoutineData(): Boolean {
        return if (mContext == null) true else presenter.attachFirebaseDatabase(mContext!!)
    }

    private fun workWithViews() {
        if (mContext != null) {
            mMarginItemDecoration = LinearMarginItemDecoration(
                    ViewUtils.getPixel(R.dimen.margin_16),
                    ViewUtils.getPixel(R.dimen.margin_16),
                    ViewUtils.getPixel(R.dimen.margin_8))

            ViewUtils.initializeRecyclerView(mBinding.recyclerViewExams, RoutineAdapter(),
                    object : ItemClickListener<Exam> {
                        override fun onItemClick(view: View, item: Exam) {
                            if (mInterstitialAd != null && mInterstitialAd?.isLoaded!!) {
                                mItem = item
                                mInterstitialAd?.show()
                            } else {
                                goToExamDetailsPage(item)
                            }
                        }
                    }
                    , null, LinearLayoutManager(mContext),
                    mMarginItemDecoration, null, DefaultItemAnimator())
        }
    }

    private fun goToExamDetailsPage(item: Exam) {
        val intent = Intent(mContext, ExamDetailsActivity::class.java)
        intent.putExtra(Constants.IntentKey.MODEL, item)
        startActivity(intent)
    }

    private fun workWithAds() {
        if (mContext != null) {
            MobileAds.initialize(mContext, DataUtils.getString(R.string.admob_app_id))
            mBinding.bannerAdView.loadAd(AdRequest.Builder().build())

            mInterstitialAd = InterstitialAd(mContext?.applicationContext)
            mInterstitialAd?.adUnitId = getString(R.string.click_on_exam_ad_unit_id)
            mInterstitialAd?.loadAd(AdRequest.Builder().build())
        }
    }

    override fun stopUI() {
        presenter.detachFirebaseDatabase()
        mInterstitialAd?.adListener = null
        mInterstitialAd = null
    }

    override fun onStop() {
        super.onStop()
        mBinding.recyclerViewExams.removeItemDecoration(mMarginItemDecoration)
    }

    private fun getAdapter(): RoutineAdapter {
        return mBinding.recyclerViewExams.adapter as RoutineAdapter
    }

    override fun onChildChanged(item: Exam) {
        getAdapter().addItem(item)
    }

    override fun onChildAdded(item: Exam) {
        getAdapter().addItem(item)
    }

    override fun onChildRemoved(item: Exam) {
        getAdapter().removeItem(item)
    }

    override fun onChildError(error: Throwable) {
        Timber.e(error)
        ToastUtils.error(getString(R.string.something_went_wrong))
    }

    override fun onInternetConnectivity(state: Boolean) {
        if (!state) {
            ToastUtils.error(getString(R.string.error_you_are_not_connected_to_the_internet))
            ProgressDialogUtils.hideProgressDialog()
        }
    }

    override fun clearTheList() {
        getAdapter().clear()
    }

    override fun setToolbarTitle(year: String) {
        val examName: String = SharedPrefUtils.get(Constants.PreferenceKey.EXAM_TYPE)!!
        setTitle(String.format(Locale.ENGLISH,
                getString(R.string.placeholder_routine_year),
                examName, year))
    }
}