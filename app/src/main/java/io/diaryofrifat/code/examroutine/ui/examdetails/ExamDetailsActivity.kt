package io.diaryofrifat.code.examroutine.ui.examdetails

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.Exam
import io.diaryofrifat.code.examroutine.ui.base.component.BaseActivity
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.DataUtils
import io.diaryofrifat.code.utils.helper.SharedPrefUtils
import io.diaryofrifat.code.utils.helper.TimeUtils
import kotlinx.android.synthetic.main.activity_exam_details.*
import java.util.*

class ExamDetailsActivity : BaseActivity<ExamDetailsMvpView, ExamDetailsPresenter>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_exam_details

    override fun getToolbarId(): Int? {
        return R.id.toolbar
    }

    override fun getActivityPresenter(): ExamDetailsPresenter {
        return ExamDetailsPresenter()
    }

    override fun startUI() {
        workWithAds()
        workWithViews()
    }

    override fun stopUI() {

    }

    private fun workWithAds() {
        MobileAds.initialize(this, DataUtils.getString(R.string.admob_app_id))
        banner_ad_view?.loadAd(AdRequest.Builder().build())
    }

    private fun workWithViews() {
        val item = extractExamDetailsData()

        if (item != null) {
            text_view_subject_name?.text = item.subjectName

            text_view_subject_code?.text = String.format(Locale.ENGLISH,
                    DataUtils.getString(R.string.placeholder_subject_code), item.subjectCode)

            val examType: String = SharedPrefUtils.get(Constants.PreferenceKey.EXAM_TYPE)!!

            text_view_time?.text = String.format(Locale.ENGLISH,
                    DataUtils.getString(R.string.placeholder_exam_time),
                    TimeUtils.getFormattedDateTimeString(item.time,
                            Constants.Common.APP_COMMON_TIME_FORMAT),
                    TimeUtils.getFormattedDateTimeString(
                            TimeUtils.getMillisecondsMinutesLater(item.time,
                                    if (examType == getString(R.string.psc)) 150 else 180),
                            Constants.Common.APP_COMMON_TIME_FORMAT))

            text_view_date?.text = TimeUtils.getFormattedDateString(item.time)
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