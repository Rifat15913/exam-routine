package io.diaryofrifat.code.examroutine.ui.selection.container

import android.os.Build
import android.view.View
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.selection.selectexam.SelectExamFragment
import io.diaryofrifat.code.utils.helper.ViewUtils

class SelectionContainerActivity : BaseActivity<SelectionContainerMvpView, SelectionContainerPresenter>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_selection_container

    override fun getActivityPresenter(): SelectionContainerPresenter {
        return SelectionContainerPresenter()
    }

    override fun startUI() {
        initialize()
        visitSelectExamType()
    }

    private fun initialize() {
        // Handle status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            ViewUtils.setStatusBarColor(this, R.color.white)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewUtils.setStatusBarColor(this, R.color.darkBackground)
        } else {
            // Do nothing for Jelly bean and Kitkat devices
        }

        window.setBackgroundDrawable(null)
    }

    override fun stopUI() {

    }

    fun visitSelectExamType() {
        commitFragment(R.id.constraint_layout_fragment_container, SelectExamFragment())
    }
}