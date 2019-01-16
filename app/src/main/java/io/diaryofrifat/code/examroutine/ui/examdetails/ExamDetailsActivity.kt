package io.diaryofrifat.code.examroutine.ui.examdetails

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.databinding.ActivityExamDetailsBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.SharedPrefUtils
import io.diaryofrifat.code.utils.helper.TimeUtils
import java.util.*

class ExamDetailsActivity : BaseActivity<ExamDetailsMvpView, ExamDetailsPresenter>() {

    private lateinit var mBinding: ActivityExamDetailsBinding

    override val layoutResourceId: Int
        get() = R.layout.activity_exam_details

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun getActivityPresenter(): ExamDetailsPresenter {
        return ExamDetailsPresenter()
    }

    override fun startUI() {
        mBinding = viewDataBinding as ActivityExamDetailsBinding
        workWithAds()
        workWithViews()
    }

    override fun stopUI() {

    }

    private fun workWithAds() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))
        mBinding.bannerAdView.loadAd(AdRequest.Builder().build())
    }

    private fun workWithViews() {
        val item = extractExamDetailsData()

        if (item != null) {
            mBinding.textViewSubjectName.text = item.subjectName

            mBinding.textViewSubjectCode.text = String.format(Locale.ENGLISH,
                    DataUtils.getString(R.string.placeholder_subject_code), item.subjectCode)

            val examType: String = SharedPrefUtils.get(Constants.PreferenceKey.EXAM_TYPE)!!

            mBinding.textViewTime.text = String.format(Locale.ENGLISH,
                    DataUtils.getString(R.string.placeholder_exam_time),
                    TimeUtils.getFormattedDateTimeString(item.time,
                            Constants.Common.APP_COMMON_TIME_FORMAT),
                    TimeUtils.getFormattedDateTimeString(
                            TimeUtils.getMillisecondsMinutesLater(item.time,
                                    if (examType == getString(R.string.psc)) 150 else 180),
                            Constants.Common.APP_COMMON_TIME_FORMAT))

            mBinding.textViewDate.text = TimeUtils.getFormattedDateString(item.time)
        }
    }

    private fun extractExamDetailsData(): Exam? {
        return intent?.extras?.getParcelable(Constants.IntentKey.MODEL)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}