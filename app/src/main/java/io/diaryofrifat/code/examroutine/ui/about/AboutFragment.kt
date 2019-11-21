package io.diaryofrifat.code.examroutine.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.base.component.BaseFragment
import io.diaryofrifat.code.utils.helper.AndroidUtils
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element
import java.util.*

class AboutFragment : BaseFragment<AboutMvpView, AboutPresenter>() {

    override val layoutId: Int
        get() = -1

    override fun getFragmentPresenter(): AboutPresenter {
        return AboutPresenter()
    }

    override fun startUI() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return AboutPage(mContext)
                .isRTL(false)
                .setImage(R.drawable.ic_exam_routine_red)
                .setDescription(getString(R.string.app_description))
                .addItem(Element().setTitle(String.format(Locale.ENGLISH,
                        getString(R.string.placeholder_version), AndroidUtils.getVersionName()))
                )
                .addEmail(getString(R.string.settings_developer_email))
                .addFacebook(getString(R.string.my_facebook))
                .addGitHub(getString(R.string.my_github))
                .create()
    }

    override fun stopUI() {

    }
}