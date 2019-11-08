package io.diaryofrifat.code.examroutine.ui.selection.container

import android.os.Build
import android.os.Bundle
import android.view.View
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.examroutine.ui.base.makeItInvisible
import io.diaryofrifat.code.examroutine.ui.base.makeItVisible
import io.diaryofrifat.code.examroutine.ui.base.setRipple
import io.diaryofrifat.code.examroutine.ui.selection.selectexam.SelectExamFragment
import io.diaryofrifat.code.examroutine.ui.selection.selectsubcategory.SelectSubcategoryFragment
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.ViewUtils
import kotlinx.android.synthetic.main.activity_selection_container.*
import java.util.*

class SelectionContainerActivity : BaseActivity<SelectionContainerMvpView, SelectionContainerPresenter>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_selection_container

    override fun getActivityPresenter(): SelectionContainerPresenter {
        return SelectionContainerPresenter()
    }

    override fun startUI() {
        initialize()
        setListeners()
        visitSelectExamType()
    }

    private fun initialize() {
        // Handle status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            ViewUtils.setStatusBarColor(this, R.color.colorWhite)
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewUtils.setStatusBarColor(this, R.color.darkBackground)
        }

        window.setBackgroundDrawable(null)
        image_view_back?.setRipple(R.color.colorPrimary26)
    }

    private fun setListeners() {
        setClickListener(image_view_back)
    }

    override fun stopUI() {

    }

    override fun onClick(view: View) {
        super.onClick(view)

        when (view.id) {
            R.id.image_view_back -> {
                onBackPressed()
            }
        }
    }

    private fun visitSelectExamType() {
        image_view_back.makeItInvisible()
        commitFragment(R.id.constraint_layout_fragment_container, SelectExamFragment())
    }

    fun visitSelectSubcategory(subcategoryList: List<ExamType>, category: ExamType) {
        image_view_back.makeItVisible()

        val fragment = SelectSubcategoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.IntentKey.CATEGORY, category)
                putParcelableArrayList(Constants.IntentKey.SUBCATEGORY,
                        subcategoryList as ArrayList<ExamType>)
            }
        }

        commitFragment(R.id.constraint_layout_fragment_container, fragment)
    }

    override fun onBackPressed() {
        if (currentFragment is SelectExamFragment) {
            super.onBackPressed()
        } else {
            visitSelectExamType()
        }
    }
}