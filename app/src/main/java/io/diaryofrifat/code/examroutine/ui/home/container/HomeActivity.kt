package io.diaryofrifat.code.examroutine.ui.home.container

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import com.google.firebase.analytics.FirebaseAnalytics
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.about.AboutFragment
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.base.setRipple
import io.diaryofrifat.code.examroutine.ui.base.toTitleCase
import io.diaryofrifat.code.examroutine.ui.examdates.ExamDatesFragment
import io.diaryofrifat.code.examroutine.ui.home.exam.ExamFragment
import io.diaryofrifat.code.utils.helper.AndroidUtils
import io.diaryofrifat.code.utils.helper.Constants
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
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            status_bar?.setBackgroundResource(R.color.colorWhite)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            status_bar?.setBackgroundResource(R.color.darkBackground)
        }

        window.setBackgroundDrawable(null)
        image_view_menu?.setRipple(R.color.colorPrimary26)

        visitExam()
    }

    private fun setPageTitle(title: String) {
        text_view_title?.text = title
    }

    private fun visitExam() {
        if (mCategory != null) {
            setPageTitle(mCategory?.examTypeTitle!!.toTitleCase(false))

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
        setClickListener(image_view_menu)

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

    override fun onClick(view: View) {
        super.onClick(view)

        when (view.id) {
            R.id.image_view_menu -> {
                drawer_layout_whole_container?.openDrawer(GravityCompat.START)
            }
        }
    }
}