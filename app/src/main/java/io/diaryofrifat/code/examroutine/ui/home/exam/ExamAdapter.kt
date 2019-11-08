package io.diaryofrifat.code.examroutine.ui.home.exam

import android.view.View
import android.view.ViewGroup
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.remote.model.Exam
import io.diaryofrifat.code.examroutine.ui.base.component.BaseAdapter
import io.diaryofrifat.code.examroutine.ui.base.component.BaseViewHolder

class ExamAdapter : BaseAdapter<Exam>() {
    override fun isEqual(left: Exam, right: Exam): Boolean {
        return left.id == right.id
    }

    override fun newViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Exam> {
        return ExamViewHolder(inflate(parent, R.layout.item_exam))
    }

    inner class ExamViewHolder(view: View) : BaseViewHolder<Exam>(view) {
        override fun bind(item: Exam) {

        }
    }
}