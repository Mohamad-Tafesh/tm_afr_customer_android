@file:JvmName("ViewUtils")

package com.tedmob.afrimoney.ui

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Dimension
import androidx.core.widget.NestedScrollView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.roundToInt

inline fun View.hideKeyboard() {
    val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    manager?.hideSoftInputFromWindow(windowToken, 0)
}

inline fun Resources.dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int =
    (dp * displayMetrics.density).roundToInt()

suspend inline fun View.awaitLayout() {
    if (isLaidOut) {
        return
    } else {
        awaitNextLayout()
    }
}

suspend inline fun View.awaitNextLayout() = suspendCancellableCoroutine<Unit> { cont ->
    val listener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int) {
            p0?.removeOnLayoutChangeListener(this)
            cont.resume(Unit)
        }
    }

    cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }

    addOnLayoutChangeListener(listener)
}


inline fun NestedScrollView.applyParallaxEffectTo(vararg views: View) {
    applyParallaxEffectTo(*views.map { it to 2f }.toTypedArray())
}

inline fun NestedScrollView.applySheetEffectTo(vararg views: View) {
    applyParallaxEffectTo(*views.map { it to 1f }.toTypedArray())
}

inline fun NestedScrollView.applyParallaxEffectTo(vararg parts: Pair<View, Float>) {
    setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
        parts.forEach { (view, ratio) -> view.translationY = scrollY / ratio }
    })
}