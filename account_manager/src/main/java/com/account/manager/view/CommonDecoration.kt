package com.account.manager.view

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Description:
 * Created by Quinin on 2020-03-25.
 **/
class CommonDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) != 0){
            //这里直接硬编码为1px
            outRect.top = 1
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

    }
}