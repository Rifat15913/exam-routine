package io.diaryofrifat.code.examroutine.ui.examdates

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.databinding.FragmentExamDatesBinding
import io.diaryofrifat.code.examroutine.ui.base.callback.ItemClickListener
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.base.helper.GridSpacingItemDecoration
import io.diaryofrifat.code.examroutine.ui.examdetails.ExamDetailsActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ProgressDialogUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import timber.log.Timber
import java.util.*

class ExamDatesFragment : BaseFragment<ExamDatesMvpView, ExamDatesPresenter>(), ExamDatesMvpView {

    private lateinit var mBinding: FragmentExamDatesBinding
    private lateinit var mMarginItemDecoration: GridSpacingItemDecoration
    private var mInterstitialAd: InterstitialAd? = null
    private var mItem: Exam? = null
    private var mExamType: ExamType? = null

    override val layoutId: Int
        get() = R.layout.fragment_exam_dates

    override fun getFragmentPresenter(): ExamDatesPresenter {
        return ExamDatesPresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as FragmentExamDatesBinding

        extractDataFromIntent()
        workWithAds()
        workWithViews()
        workWithRoutineData()
        setListeners()
        workWithValidation()
    }

    private fun extractDataFromIntent() {
        val bundle = arguments
        if (bundle != null && bundle.containsKey(ExamDatesFragment::class.java.simpleName)) {
            mExamType = bundle.getParcelable(ExamDatesFragment::class.java.simpleName)
        }
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

    private fun workWithRoutineData() {
        if (mExamType != null) {
            presenter.attachFirebaseDatabase(mContext!!, mExamType!!)
        } else {
            activity?.finish()
        }
    }

    private fun workWithViews() {
        if (mContext != null) {
            mMarginItemDecoration =
                    GridSpacingItemDecoration(3, ViewUtils.getPixel(R.dimen.margin_16), true)

            ViewUtils.initializeRecyclerView(mBinding.recyclerViewExams, ExamDatesAdapter(),
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
                    , null, GridLayoutManager(mContext, 3),
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

    private fun getAdapter(): ExamDatesAdapter {
        return mBinding.recyclerViewExams.adapter as ExamDatesAdapter
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
        if (mExamType != null) {
            setTitle(String.format(Locale.ENGLISH,
                    getString(R.string.placeholder_routine_year),
                    mExamType?.examType, year))
        }
    }
}