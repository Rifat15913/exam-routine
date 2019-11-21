package io.diaryofrifat.code.examroutine.ui.home.settings

import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment

class SettingsContainerFragment
    : BaseFragment<SettingsContainerMvpView, SettingsContainerPresenter>() {

    override val layoutId: Int
        get() = R.layout.fragment_settings_container

    override fun getFragmentPresenter(): SettingsContainerPresenter {
        return SettingsContainerPresenter()
    }

    override fun startUI() {

    }

    override fun stopUI() {

    }
}