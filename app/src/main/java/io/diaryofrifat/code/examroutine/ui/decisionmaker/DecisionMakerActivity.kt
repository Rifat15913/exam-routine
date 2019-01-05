package io.diaryofrifat.code.examroutine.ui.decisionmaker

import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ActivityDecisionMakerBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity

class DecisionMakerActivity : BaseActivity<DecisionMakerMvpView, DecisionMakerPresenter>() {

    private lateinit var mBinding: ActivityDecisionMakerBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_decision_maker

    override fun getActivityPresenter(): DecisionMakerPresenter {
        return DecisionMakerPresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as ActivityDecisionMakerBinding
    }

    override fun stopUI() {

    }
}