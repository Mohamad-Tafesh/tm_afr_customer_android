package com.tedmob.africell.features.settings.menu

import android.animation.TypeEvaluator
import android.annotation.SuppressLint
import android.graphics.Rect

class RectEvaluatorCompat(private val max: Rect) : TypeEvaluator<Rect> {

    private val mTemp = Rect()

    @SuppressLint("CheckResult")
    override fun evaluate(fraction: Float, startValue: Rect, endValue: Rect): Rect {
        mTemp.left = startValue.left + ((endValue.left - startValue.left) * fraction).toInt()
        mTemp.top = startValue.top + ((endValue.top - startValue.top) * fraction).toInt()
        mTemp.right = startValue.right + ((endValue.right - startValue.right) * fraction).toInt()
        mTemp.bottom =
            startValue.bottom + ((endValue.bottom - startValue.bottom) * fraction).toInt()

        mTemp.setIntersect(max, mTemp)

        return mTemp
    }
}