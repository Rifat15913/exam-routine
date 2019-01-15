package io.diaryofrifat.code.examroutine.ui.home

import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ActivityHomeBinding
import io.diaryofrifat.code.examroutine.ui.about.AboutFragment
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.routine.RoutineFragment
import io.diaryofrifat.code.utils.helper.AndroidUtils
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.SharedPrefUtils
import io.diaryofrifat.code.utils.libs.ToastUtils
import timber.log.Timber
import java.util.*


class HomeActivity : BaseActivity<HomeMvpView, HomePresenter>() {

    private lateinit var mBinding: ActivityHomeBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_home

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun getActivityPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun startUI() {
        workWithViews()
        setListeners()
    }

    private fun workWithViews() {
        mBinding = viewDataBinding as ActivityHomeBinding
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        launchHomePage()
    }

    private fun launchHomePage() {
        val examName: String = SharedPrefUtils.get(Constants.PreferenceKey.EXAM_TYPE)!!
        setTitle(String.format(Locale.ENGLISH,
                getString(R.string.placeholder_routine),
                examName))
        commitFragment(R.id.constraint_layout_fragment_container, RoutineFragment())
    }

    private fun launchAboutPage() {
        setTitle(getString(R.string.nav_about))
        commitFragment(R.id.constraint_layout_fragment_container, AboutFragment())
    }

    private fun setListeners() {
        mBinding.navigationViewDrawerMenu.setNavigationItemSelectedListener {
            mBinding.drawerLayoutWholeContainer.closeDrawers()

            when (it.itemId) {
                R.id.nav_home -> {
                    if (currentFragment !is RoutineFragment) {
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
                    } catch (e: android.content.ActivityNotFoundException) {
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
            }

            false
        }
    }

    override fun stopUI() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mBinding.drawerLayoutWholeContainer.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}