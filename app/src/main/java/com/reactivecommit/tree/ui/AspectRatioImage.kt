package com.reactivecommit.tree.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class AspectRatioImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    AppCompatImageView(context, attrs, defStyle) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (drawable != null && drawable.intrinsicHeight != 0) {
            val ratio = drawable.intrinsicWidth.toDouble() / drawable.intrinsicHeight.toDouble()
            setMeasuredDimension(measuredWidth, (measuredWidth / ratio).toInt())
        }
    }
}