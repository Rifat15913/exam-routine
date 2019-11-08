package io.diaryofrifat.code.examroutine.ui.home.container

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import com.google.firebase.analytics.FirebaseAnalytics
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.about.AboutFragment
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.examdates.ExamDatesFragment
import io.diaryofrifat.code.utils.helper.AndroidUtils
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.libs.ToastUtils
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber
import java.util.*


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
            })
        }
    }

    private var mExamType: ExamType? = null

    override val layoutResourceId: Int
        get() = R.layout.activity_home

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun getActivityPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun startUI() {
        extractDataFromIntent()
        workWithViews()
        setListeners()
    }

    private fun extractDataFromIntent() {
        if (intent != null && intent.hasExtra(HomeActivity::class.java.simpleName)) {
            // mExamType = intent.getParcelableExtra(HomeActivity::class.java.simpleName)
        }
    }

    private fun workWithViews() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        // launchHomePage()
    }

    private fun launchHomePage() {
        if (mExamType != null) {
            setTitle(String.format(Locale.ENGLISH,
                    getString(R.string.placeholder_routine),
                    mExamType?.examTypeTitle))

            val fragment = ExamDatesFragment()
            val arguments = Bundle()
            // arguments.putParcelable(ExamDatesFragment::class.java.simpleName, mExamType)
            fragment.arguments = arguments
            commitFragment(R.id.constraint_layout_fragment_container, fragment)
        }
    }

    private fun launchAboutPage() {
        setTitle(getString(R.string.nav_about))
        commitFragment(R.id.constraint_layout_fragment_container, AboutFragment())
    }

    private fun setListeners() {
        navigation_view_drawer_menu?.setNavigationItemSelectedListener {
            drawer_layout_whole_container?.closeDrawers()

            when (it.itemId) {
                R.id.nav_home -> {
                    if (currentFragment !is ExamDatesFragment) {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.nav_routine))
                        // FirebaseUtils.getFirebaseAnalytics()?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

                        launchHomePage()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout_whole_container?.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}