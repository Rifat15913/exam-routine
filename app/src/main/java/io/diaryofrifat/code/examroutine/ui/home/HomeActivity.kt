package io.diaryofrifat.code.examroutine.ui.home

import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.routine.RoutineFragment

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
        commitFragment(R.id.constraint_layout_fragment_container, RoutineFragment())
    }

    override fun stopUI() {

    }
}