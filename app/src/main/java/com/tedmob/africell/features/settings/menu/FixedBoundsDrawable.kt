package com.tedmob.africell.features.settings.menu

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * A wrapped [Drawable] that force use its own bounds to draw.
 *
 * It maybe a little dirty. But if we don't do that, during the expanding animation, there will be
 * one or two frame using wrong bounds because of parent view sets bounds.
 */
class FixedBoundsDrawable(private val mDrawable: Drawable) : Drawable(), Drawable.Callback {

    private val internalFixedBounds = Rect()


    var fixedBounds: Rect
        get() = internalFixedBounds
        set(bounds) {
            setFixedBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
        }

    fun setFixedBounds(left: Int, top: Int, right: Int, bottom: Int) {
        internalFixedBounds.set(left, top, right, bottom)
        setBounds(left, top, right, bottom)
    }

    override fun getOutline(outline: Outline) {
        mDrawable.getOutline(outline)
    }

    override fun draw(canvas: Canvas) {
        mDrawable.bounds = internalFixedBounds
        mDrawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        mDrawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mDrawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int = mDrawable.opacity

    override fun invalidateDrawable(who: Drawable) {
        callback?.invalidateDrawable(this)
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        callback?.scheduleDrawable(this, what, `when`)
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        callback?.unscheduleDrawable(this, what)
    }
}
