package io.diaryofrifat.code.examroutine.ui.datedetails

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ActivityDateDetailsBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.DataUtils

class DateDetailsActivity : BaseActivity<DateDetailsMvpView, DateDetailsPresenter>() {
    private lateinit var mBinding: ActivityDateDetailsBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_date_details

    override fun getActivityPresenter(): DateDetailsPresenter {
        return DateDetailsPresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as ActivityDateDetailsBinding
        workWithAds()
    }

    override fun stopUI() {

    }

    private fun workWithAds() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))
        mBinding.bannerAdView.loadAd(AdRequest.Builder().build())
    }
}