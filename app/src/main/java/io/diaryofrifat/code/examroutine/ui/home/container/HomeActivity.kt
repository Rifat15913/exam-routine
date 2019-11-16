package io.diaryofrifat.code.examroutine.ui.home.container

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.ShareCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.about.AboutFragment
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.examdates.ExamDatesFragment
import io.diaryofrifat.code.examroutine.ui.home.exam.ExamFragment
import io.diaryofrifat.code.examroutine.ui.selection.container.SelectionContainerActivity
import io.diaryofrifat.code.utils.helper.AndroidUtils
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.ViewUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber


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
        loadAd()
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

    private fun loadAd() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))
        banner_ad_view?.loadAd(AdRequest.Builder().build())
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

    private fun launchAboutPage() {
        setTitle(getString(R.string.nav_about))
        commitFragment(R.id.constraint_layout_fragment_container, AboutFragment())
    }

    private fun setListeners() {
        bottom_bar?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_routines -> {
                    visitExam()
                }

                R.id.action_settings -> {

                }
            }

            true
        }

        navigation_view_drawer_menu?.setNavigationItemSelectedListener {
            drawer_layout_whole_container?.closeDrawers()

            when (it.itemId) {
                R.id.nav_home -> {
                    if (currentFragment !is ExamDatesFragment) {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.nav_routine))
                        // FirebaseUtils.getFirebaseAnalytics()?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

                        visitExam()
                    }
                }

                R.id.nav_change_exam_type -> {
                    finish()
                }

                R.id.nav_rate_it -> {

                    try {
                        startActivity(Intent(Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.market_link)
                                        + AndroidUtils.getApplicationId())))
                    } catch (e: ActivityNotFoundException) {
                        Timber.e(e)
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW,
                                    Uri.parse(getString(R.string.play_store_link)
                                            + AndroidUtils.getApplicationId())))
                        } catch (e: Exception) {
                            Timber.e(e)
                            ToastUtils.error(getString(R.string.error_could_not_find_the_application))
                        }
                    }
                }

                R.id.nav_share_the_app -> {
                    ShareCompat.IntentBuilder.from(this)
                            .setType("text/plain")
                            .setChooserTitle(getString(R.string.content_share_the_app))
                            .setText((getString(R.string.play_store_link)
                                    + AndroidUtils.getApplicationId()))
                            .startChooser()
                }

                R.id.nav_about -> {
                    launchAboutPage()
                }

                R.id.nav_privacy_policy -> {
                    startActivity(Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.privacy_policy_link)))
                    )
                }
            }

            false
        }
    }

    override fun stopUI() {

    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Handle bottom bar use-cases
        SelectionContainerActivity.startActivity(this)
    }
}