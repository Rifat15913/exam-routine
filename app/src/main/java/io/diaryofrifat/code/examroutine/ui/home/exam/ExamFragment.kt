package io.diaryofrifat.code.examroutine.ui.home.exam

import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.examroutine.ui.home.container.HomeActivity
import io.diaryofrifat.code.utils.helper.Constants

class ExamFragment : BaseFragment<ExamMvpView, ExamPresenter>() {

    private var mCategory: ExamType? = null
    private var mSubcategory: ExamType? = null

    override val layoutId: Int
        get() = R.layout.fragment_exam

    override fun getFragmentPresenter(): ExamPresenter {
        return ExamPresenter()
    }

    override fun startUI() {
        extractDataFromArguments()
    }

    private fun extractDataFromArguments() {
        val args = arguments
        if (args != null
                && args.containsKey(Constants.IntentKey.CATEGORY)
        ) {
            mCategory = args.getParcelable(Constants.IntentKey.CATEGORY)

            if (args.containsKey(Constants.IntentKey.SUBCATEGORY)) {
                mSubcategory = args.getParcelable(Constants.IntentKey.SUBCATEGORY)
            }
        } else {
            (activity as HomeActivity).onBackPressed()
        }
    }

    override fun stopUI() {

    }
}