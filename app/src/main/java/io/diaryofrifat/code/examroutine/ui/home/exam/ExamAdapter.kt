package io.diaryofrifat.code.examroutine.ui.home.exam

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.ui.base.component.BaseAdapter
import io.diaryofrifat.code.examroutine.ui.base.component.BaseViewHolder
import io.diaryofrifat.code.examroutine.ui.base.getString
import io.diaryofrifat.code.examroutine.ui.base.makeItInvisible
import io.diaryofrifat.code.examroutine.ui.base.makeItVisible
import io.diaryofrifat.code.utils.helper.Constants
import io.diaryofrifat.code.utils.helper.TimeUtils
import kotlinx.android.synthetic.main.item_exam.view.*
import java.util.*

class ExamAdapter : BaseAdapter<Exam>() {
    override fun isEqual(left: Exam, right: Exam): Boolean {
        return left.id == right.id
    }

    override fun newViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Exam> {
        return ExamViewHolder(inflate(parent, R.layout.item_exam))
    }

    inner class ExamViewHolder(view: View) : BaseViewHolder<Exam>(view) {
        override fun bind(item: Exam) {
            itemView.text_view_subject_name?.text = item.subjectName

            if (!TextUtils.isEmpty(item.subjectCode)) {
                itemView.text_view_subject_code?.makeItVisible()
                itemView.text_view_subject_code?.text =
                        String.format(
                                Locale.getDefault(),
                                getString(R.string.exam_placeholder_subject_code),
                                item.subjectCode
                        )
            } else {
                itemView.text_view_subject_code?.makeItInvisible()
            }

            itemView.chip_subcategory?.text =
                    if (item.subcategory == null) {
                        getString(R.string.exam_na_subcategory)
                    } else {
                        item.subcategory?.examTypeTitle
                    }

            itemView.text_view_time?.text =
                    String.format(Locale.ENGLISH,
                            getString(R.string.placeholder_exam_time),
                            TimeUtils.getFormattedDateTimeString(item.startingTime,
                                    Constants.Common.APP_COMMON_TIME_FORMAT),
                            TimeUtils.getFormattedDateTimeString(item.endingTime,
                                    Constants.Common.APP_COMMON_TIME_FORMAT))

            itemView.text_view_day?.text = TimeUtils.getFormattedDayNameString(item.startingTime)
            itemView.text_view_date?.text = TimeUtils.getFormattedOnlyDateString(item.startingTime)
            itemView.text_view_month?.text = TimeUtils.getFormattedMonthString(item.startingTime)
        }
    }
}