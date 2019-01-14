package io.diaryofrifat.code.examroutine.ui.home

import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.routine.RoutineFragment
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.SharedPrefUtils
import java.util.*

class HomeActivity : BaseActivity<HomeMvpView, HomePresenter>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_home

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun getActivityPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun startUI() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)

        val examName: String = SharedPrefUtils.get(Constants.PreferenceKey.EXAM_TYPE)!!
        setTitle(String.format(Locale.ENGLISH,
                getString(R.string.placeholder_routine),
                examName))
        commitFragment(R.id.constraint_layout_fragment_container, RoutineFragment())
    }

    override fun stopUI() {

    }
}