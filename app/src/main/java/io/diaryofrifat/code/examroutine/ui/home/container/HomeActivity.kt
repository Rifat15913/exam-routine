package io.diaryofrifat.code.examroutine.ui.home.container

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.home.exam.ExamFragment
import io.diaryofrifat.code.examroutine.ui.home.settings.SettingsContainerFragment
import io.diaryofrifat.code.examroutine.ui.selection.container.SelectionContainerActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity<HomeMvpView, HomePresenter>() {

    companion object {
        /**
         * This method starts current activity
         *
         * @param context UI context
         * */
        fun startActivity(context: Context, category: ExamType, subcategory: ExamType?) {
            runCurrentActivity(context, Intent(context, HomeActivity::class.java).apply {
                putExtra(Constants.IntentKey.CATEGORY, category)
                if (subcategory != null) {
                    putExtra(Constants.IntentKey.SUBCATEGORY, subcategory)
                }

                this.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private var mInterstitialAd: InterstitialAd? = null
    private var mCategory: ExamType? = null
    private var mSubcategory: ExamType? = null

    override val layoutResourceId: Int
        get() = R.layout.activity_home

    override fun getActivityPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun startUI() {
        extractDataFromIntent()
        initialize()
        setListeners()
    }

    private fun extractDataFromIntent() {
        if (intent != null
                && intent.hasExtra(Constants.IntentKey.CATEGORY)) {
            mCategory = intent.getParcelableExtra(Constants.IntentKey.CATEGORY)

            if (intent.hasExtra(Constants.IntentKey.SUBCATEGORY)) {
                mSubcategory = intent.getParcelableExtra(Constants.IntentKey.SUBCATEGORY)
            }
        } else {
            onBackPressed()
        }
    }

    private fun initialize() {
        // Handle status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ViewUtils.setLightStatusBar(this)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewUtils.setStatusBarColor(this, R.color.darkBackground)
        }

        window.setBackgroundDrawable(null)

        visitExam()
    }

    private fun visitExam() {
        if (mCategory != null) {
            val fragment = ExamFragment().apply {
                val args = Bundle()
                args.putParcelable(Constants.IntentKey.CATEGORY, mCategory)
                mSubcategory?.let {
                    args.putParcelable(Constants.IntentKey.SUBCATEGORY, mSubcategory)
                }
                arguments = args
            }
            commitFragment(R.id.constraint_layout_fragment_container, fragment)
        }
    }

    private fun visitSettings() {
        val fragment = SettingsContainerFragment()
        commitFragment(R.id.constraint_layout_fragment_container, fragment)
    }

    private fun setListeners() {
        bottom_bar?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_routines -> {
                    visitExam()
                }

                R.id.action_settings -> {
                    if (mInterstitialAd?.isLoaded!!) {
                        mInterstitialAd?.show()
                    } else {
                        visitSettings()
                    }
                }
            }

            true
        }
    }

    override fun stopUI() {
        mInterstitialAd?.adListener = null
        mInterstitialAd = null
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Handle bottom bar use-cases
        if (currentFragment is ExamFragment) {
            SelectionContainerActivity.startActivity(this)
        } else {
            visitExam()
        }
    }

    override fun onStart() {
        super.onStart()
        loadAd()
    }

    private fun loadAd() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd?.adUnitId = getString(R.string.settings_ad_unit_id)
        mInterstitialAd?.loadAd(AdRequest.Builder().build())

        mInterstitialAd?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                visitSettings()
            }
        }
    }
}