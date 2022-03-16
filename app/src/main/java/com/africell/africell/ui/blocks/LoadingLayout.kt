package com.africell.africell.ui.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class LoadingLayout : FrameLayout {

    private var showContent = false

    val loadingView: LoadingView by lazy { LoadingView(context) }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        addView(loadingView)
        updateVisibility()

        post { updateVisibility() }
    }

    fun updateVisibility() {
        for (i in 0 until childCount) {
            getChildAt(i).visibility = if (showContent) View.VISIBLE else View.GONE
        }
        loadingView.visibility = if (showContent) View.GONE else View.VISIBLE
    }

    fun showContent() {
        showContent = true
        updateVisibility()
    }



    fun showLoadingView() {
        showContent = false
        updateVisibility()
    }

}