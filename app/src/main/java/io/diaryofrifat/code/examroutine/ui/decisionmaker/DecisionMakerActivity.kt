package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ActivityDecisionMakerBinding
import io.diaryofrifat.code.examroutine.ui.base.callback.ItemClickListener
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.base.helper.LinearMarginItemDecoration
import io.diaryofrifat.code.examroutine.ui.home.HomeActivity
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.firebase.FirebaseUtils


class DecisionMakerActivity : BaseActivity<DecisionMakerMvpView, DecisionMakerPresenter>() {

    private lateinit var mBinding: ActivityDecisionMakerBinding
    private var mInterstitialAd: InterstitialAd? = null
    private var mSelectedExamType: String? = null
    private var mItemDecoration: LinearMarginItemDecoration? = null

    override val layoutResourceId: Int
        get() = R.layout.activity_decision_maker

    override fun getActivityPresenter(): DecisionMakerPresenter {
        return DecisionMakerPresenter()
    }

    override fun startUI() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))
        initialization()
        setListeners()
    }

    private fun initialization() {
        loadViews()
        loadData()
    }

    private fun loadViews() {
        mBinding = viewDataBinding as ActivityDecisionMakerBinding
        window.setBackgroundDrawable(null)

        // Handle status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            mBinding.customStatusBarView.setBackgroundColor(ViewUtils.getColor(R.color.white))
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBinding.customStatusBarView.setBackgroundColor(ViewUtils.getColor(R.color.colorPrimaryDark))
        } else {
            // Do nothing for Jelly bean and Kitkat devices
        }

        mItemDecoration = LinearMarginItemDecoration(ViewUtils.dpToPx(16).toInt())

        ViewUtils.initializeRecyclerView(
                mBinding.recyclerViewExamTypes,
                ExamTypeAdapter(),
                object : ItemClickListener<String> {
                    override fun onItemClick(view: View, item: String) {
                        clickOnItem(item)
                    }
                },
                null,
                LinearLayoutManager(this),
                mItemDecoration,
                null,
                null)
    }

    private fun loadData() {
        loadAd()
    }

    private fun loadAd() {
        mInterstitialAd = InterstitialAd(applicationContext)
        mInterstitialAd?.adUnitId = getString(R.string.before_routine_ad_unit_id)
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun setListeners() {
        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                val bundleAd = Bundle()
                bundleAd.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.interstitial_ad_decision_maker_page))
                bundleAd.putBoolean(FirebaseAnalytics.Param.ITEM_CATEGORY, true)
                FirebaseUtils.getFirebaseAnalytics()?.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundleAd)
                goToHomePage()
            }
        }
    }

    override fun stopUI() {
        mInterstitialAd?.adListener = null
        mInterstitialAd = null
    }

    private fun clickOnItem(item: String) {
        mSelectedExamType = item

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mSelectedExamType)
        FirebaseUtils.getFirebaseAnalytics()?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        if (mInterstitialAd?.isLoaded!!) {
            mInterstitialAd?.show()
        } else {
            val bundleAd = Bundle()
            bundleAd.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.interstitial_ad_decision_maker_page))
            bundleAd.putBoolean(FirebaseAnalytics.Param.ITEM_CATEGORY, false)
            FirebaseUtils.getFirebaseAnalytics()?.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundleAd)
            goToHomePage()
        }
    }

    private fun goToHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (mSelectedExamType != null) {
            intent.putExtra(HomeActivity::class.java.simpleName, mSelectedExamType)
        }

        startActivity(intent)
    }
}