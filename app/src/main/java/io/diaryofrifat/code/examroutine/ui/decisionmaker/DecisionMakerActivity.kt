package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.content.Intent
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.button.MaterialButton
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ActivityDecisionMakerBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.home.HomeActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.SharedPrefUtils

class DecisionMakerActivity : BaseActivity<DecisionMakerMvpView, DecisionMakerPresenter>() {

    private lateinit var mBinding: ActivityDecisionMakerBinding
    private var mInterstitialAd: InterstitialAd? = null

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
        mBinding = viewDataBinding as ActivityDecisionMakerBinding
        window.setBackgroundDrawable(null)
        mInterstitialAd = InterstitialAd(applicationContext)
        mInterstitialAd?.adUnitId = getString(R.string.before_routine_ad_unit_id)
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun setListeners() {
        setClickListener(mBinding.buttonPsc, mBinding.buttonJsc,
                mBinding.buttonSsc, mBinding.buttonHsc)

        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                goToHomePage()
            }
        }
    }

    override fun stopUI() {
        mInterstitialAd?.adListener = null
        mInterstitialAd = null
    }

    override fun onClick(view: View) {
        super.onClick(view)

        SharedPrefUtils.set(Constants.PreferenceKey.EXAM_TYPE,
                (view as MaterialButton).text.toString())

        if (mInterstitialAd?.isLoaded!!) {
            mInterstitialAd?.show()
        } else {
            goToHomePage()
        }
    }

    private fun goToHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}