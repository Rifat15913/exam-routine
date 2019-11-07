package io.diaryofrifat.code.examroutine.ui.datedetails

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.DataUtils
import kotlinx.android.synthetic.main.activity_date_details.*

class DateDetailsActivity : BaseActivity<DateDetailsMvpView, DateDetailsPresenter>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_date_details

    override fun getActivityPresenter(): DateDetailsPresenter {
        return DateDetailsPresenter()
    }

    override fun startUI() {
        workWithAds()
    }

    override fun stopUI() {

    }

    private fun workWithAds() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))
        banner_ad_view?.loadAd(AdRequest.Builder().build())
    }
}