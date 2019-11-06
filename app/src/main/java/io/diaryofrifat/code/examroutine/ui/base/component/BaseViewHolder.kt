package io.diaryofrifat.code.examroutine.ui.base.component

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    /**
     * This method binds the item to item layout
     *
     * @param item model object
     * */
    abstract fun bind(item: T)

    /**
     * This method sets click listener to passed view/s
     *
     * @param views View as params
     */
    protected fun setClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    /**
     * This method is fired upon clicking on any view of the item layout
     *
     * @param view clicked view
     * */
    override fun onClick(view: View) {

    }
}