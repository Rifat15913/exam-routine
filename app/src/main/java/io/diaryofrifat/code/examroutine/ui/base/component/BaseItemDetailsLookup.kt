package com.lusosmile.main.ui.base.component

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import io.diaryofrifat.code.examroutine.ui.base.component.BaseSelectableViewHolder

/**
 * This is base item details lookup class which will be used for creating selectable RecyclerView
 * @author Mohd. Asfaq-E-Azam Rifat
 * */
class BaseItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as BaseSelectableViewHolder<*>)
                    .getItemDetails()
        }

        return null
    }
}