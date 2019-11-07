package io.diaryofrifat.code.examroutine.ui.selectexam

import android.view.View
import android.view.ViewGroup
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.ui.base.component.BaseAdapter
import io.diaryofrifat.code.examroutine.ui.base.component.BaseViewHolder
import kotlinx.android.synthetic.main.item_option.view.*

class SelectExamAdapter : BaseAdapter<ExamType>() {
    override fun isEqual(left: ExamType, right: ExamType): Boolean {
        return left.id == right.id
    }

    override fun newViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ExamType> {
        return ExamTypeViewHolder(inflate(parent, R.layout.item_option))
    }

    inner class ExamTypeViewHolder(view: View) : BaseViewHolder<ExamType>(view) {

        override fun bind(item: ExamType) {
            itemView.text_view_title?.text = item.examTypeTitle
            setClickListener(itemView)
        }

        override fun onClick(view: View) {
            super.onClick(view)

            getItem(adapterPosition)?.let {
                mItemClickListener?.onItemClick(view, it, adapterPosition)
            }
        }
    }
}