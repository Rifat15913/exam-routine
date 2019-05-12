package io.diaryofrifat.code.examroutine.ui.decisionmaker

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import io.diaryofrifat.code.examroutine.R
import io.diaryofrifat.code.examroutine.databinding.ItemExamTypeBinding
import io.diaryofrifat.code.examroutine.ui.base.component.BaseAdapter
import io.diaryofrifat.code.examroutine.ui.base.component.BaseViewHolder

class ExamTypeAdapter : BaseAdapter<String>() {
    override fun isEqual(left: String, right: String): Boolean {
        return left == right
    }

    override fun newViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        return ExamTypeViewHolder(inflate(parent, R.layout.item_exam_type))
    }

    inner class ExamTypeViewHolder(binding: ViewDataBinding) : BaseViewHolder<String>(binding) {
        private val mBinding = binding as ItemExamTypeBinding

        override fun bind(item: String) {
            mBinding.materialButtonExamType.text = item
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (getItem(adapterPosition) != null) {
                mItemClickListener?.onItemClick(view, getItem(adapterPosition)!!)
            }
        }
    }
}