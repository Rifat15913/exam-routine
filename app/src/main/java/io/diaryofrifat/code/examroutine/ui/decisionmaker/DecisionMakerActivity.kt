package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.view.View
import com.google.android.material.button.MaterialButton
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ActivityDecisionMakerBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.SharedPrefUtils

class DecisionMakerActivity : BaseActivity<DecisionMakerMvpView, DecisionMakerPresenter>() {

    private lateinit var mBinding: ActivityDecisionMakerBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_decision_maker

    override fun getActivityPresenter(): DecisionMakerPresenter {
        return DecisionMakerPresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as ActivityDecisionMakerBinding
        window.setBackgroundDrawable(null)

        setClickListener(mBinding.buttonPsc, mBinding.buttonJsc,
                mBinding.buttonSsc, mBinding.buttonHsc)
    }

    override fun stopUI() {

    }

    override fun onClick(view: View) {
        super.onClick(view)

        SharedPrefUtils.set(Constants.PreferenceKey.EXAM_TYPE,
                (view as MaterialButton).text.toString())
    }
}