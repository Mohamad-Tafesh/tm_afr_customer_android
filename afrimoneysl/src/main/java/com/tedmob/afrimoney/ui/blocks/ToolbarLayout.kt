package com.tedmob.afrimoney.ui.blocks

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar
import com.tedmob.afrimoney.R

class ToolbarLayout : LinearLayoutCompat {
    lateinit var toolbarLayout: View
    lateinit var toolbar: Toolbar

    constructor(context: Context, @LayoutRes toolbarLayoutId: Int, @IdRes toolbarId: Int) : super(context) {
        extractToolbarFromRes(context, toolbarLayoutId, toolbarId)
        init()
    }

    constructor(context: Context, toolbarLayoutView: View, @IdRes toolbarId: Int) : super(context) {
        extractToolbar({ toolbarLayoutView }, toolbarId)
        init()
    }

    private fun extractToolbarFromRes(
        context: Context,
        @LayoutRes toolbarLayoutResId: Int,
        @IdRes toolbarId: Int
    ) {
        extractToolbar(
            { LayoutInflater.from(context).inflate(toolbarLayoutResId, this, false) },
            toolbarId
        )
    }

    private fun extractToolbar(
        toolbarLayoutProvider: () -> View,
        @IdRes toolbarId: Int
    ) {
        toolbarLayout = toolbarLayoutProvider()
        val view = toolbarLayout.findViewById<View>(toolbarId)
        toolbar =
            view as? Toolbar
                ?: throw IllegalArgumentException(
                    "toolbar was: ${view.javaClass.name}, but must be either: ${Toolbar::class.java.name} or ${MaterialToolbar::class.java.name}"
                )
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.toolbarLayoutStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val theme = context.theme
        val a = theme.obtainStyledAttributes(attrs, R.styleable.ToolbarLayout, defStyleAttr, -1)

        val toolbarLayoutId = a.getResourceId(R.styleable.ToolbarLayout_toolbarLayout, R.layout.toolbar_default)
        val toolbarId = a.getResourceId(R.styleable.ToolbarLayout_toolbar, R.id.toolbar)

        extractToolbarFromRes(context, toolbarLayoutId, toolbarId)

        a.recycle()
        init()
    }


    private fun init() {
        orientation = VERTICAL
        addView(toolbarLayout)
    }

}