package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.button.MaterialButton
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ActivityDecisionMakerBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.Constants
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
        initialization()
        setListeners()
    }

    private fun initialization() {
        mBinding = viewDataBinding as ActivityDecisionMakerBinding
        window.setBackgroundDrawable(null)
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd?.adUnitId = getString(R.string.admob_before_routine_interstitial_ad_unit_id)
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun setListeners() {
        setClickListener(mBinding.buttonPsc, mBinding.buttonJsc,
                mBinding.buttonSsc, mBinding.buttonHsc)

        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                goToRoutinePage()
            }
        }
    }

    override fun stopUI() {

    }

    override fun onClick(view: View) {
        super.onClick(view)

        SharedPrefUtils.set(Constants.PreferenceKey.EXAM_TYPE,
                (view as MaterialButton).text.toString())

        if (mInterstitialAd?.isLoaded!!) {
            mInterstitialAd?.show()
        } else {
            goToRoutinePage()
        }
    }

    private fun goToRoutinePage() {

    }
}