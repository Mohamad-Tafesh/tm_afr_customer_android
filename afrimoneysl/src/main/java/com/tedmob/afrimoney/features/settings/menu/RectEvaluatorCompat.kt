package com.tedmob.afrimoney.features.settings.menu

import android.animation.TypeEvaluator
import android.annotation.SuppressLint
import android.graphics.Rect

class RectEvaluatorCompat(private val max: Rect) : TypeEvaluator<Rect> {

    private val mTemp = Rect()

    @SuppressLint("CheckResult")
    override fun evaluate(fraction: Float, startValue: Rect, endValue: Rect): Rect {
        mTemp.set(
            startValue.left + ((endValue.left - startValue.left) * fraction).toInt(),
            startValue.top + ((endValue.top - startValue.top) * fraction).toInt(),
            startValue.right + ((endValue.right - startValue.right) * fraction).toInt(),
            startValue.bottom + ((endValue.bottom - startValue.bottom) * fraction).toInt()
        )

        mTemp.setIntersect(max, mTemp)

        return mTemp
    }
}