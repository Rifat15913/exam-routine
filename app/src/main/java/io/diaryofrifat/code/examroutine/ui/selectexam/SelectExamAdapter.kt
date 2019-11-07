package io.diaryofrifat.code.examroutine.ui.selectexam

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.data.local.ExamType
import io.diaryofrifat.code.examroutine.databinding.ItemExamTypeBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseSelectableAdapter
import io.diaryofrifat.code.examroutine.ui.base.component.BaseViewHolder

class SelectExamAdapter : BaseSelectableAdapter<ExamType>() {
    override fun getItemIdForPosition(position: Int): Long {
        return getItem(position)?.id!!
    }

    override fun isEqual(left: ExamType, right: ExamType): Boolean {
        return left.id == right.id
    }

    override fun newViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ExamType> {
        return ExamTypeViewHolder(inflate(parent, R.layout.item_exam_type))
    }

    inner class ExamTypeViewHolder(binding: ViewDataBinding) : BaseViewHolder<ExamType>(binding) {
        private val mBinding = binding as ItemExamTypeBinding

        override fun bind(item: ExamType) {
            mBinding.materialButtonExamType.text = item.examType
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (getItem(adapterPosition) != null) {
                mItemClickListener?.onItemClick(view, getItem(adapterPosition)!!)
            }
        }
    }
}