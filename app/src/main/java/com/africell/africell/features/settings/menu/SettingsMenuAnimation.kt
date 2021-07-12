package com.africell.africell.features.settings.menu

import android.animation.*
import android.graphics.Rect
import android.util.Property
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun SettingsMenuPopupWindow.postStartEnterAnimation(
    background: FixedBoundsDrawable,
    width: Int,
    height: Int,
    startX: Int,
    startY: Int,
    start: Rect?,
    itemHeight: Int,
    elevation: Int,
    selectedIndex: Int
) {
    this.background.fixedBounds = Rect()
    contentView.clipBounds = Rect()

    contentView.post(Runnable {
        // return if already dismissed
        if (contentView.parent == null) {
            return@Runnable
        }

        contentView.startEnterAnimation(
            background,
            width,
            height,
            startX,
            startY,
            start,
            itemHeight,
            elevation,
            selectedIndex
        )
    })
}

fun View.startEnterAnimation(
    background: FixedBoundsDrawable,
    width: Int,
    height: Int,
    centerX: Int,
    centerY: Int,
    start: Rect?,
    itemHeight: Int,
    elevation: Int,
    selectedIndex: Int
) {
    val holder =
        PropertyHolder(
            background,
            this
        )
    val backgroundAnimator = holder.createBoundsAnimator(width, height, centerX, centerY, start)
    val animatorSet = AnimatorSet()

    animatorSet.playTogether(
        backgroundAnimator,
        createElevationAnimator(
            parent as View,
            elevation.toFloat()
        )
    )

    animatorSet.duration = backgroundAnimator.duration
    animatorSet.start()

    val delay: Long = 0
    if (this is ViewGroup) {
        for (i in 0 until childCount) {
            val offset = selectedIndex - i
            startChild(
                getChildAt(i),
                delay + 30 * abs(offset),
                if (offset == 0) 0 else (itemHeight * 0.2).toInt() * if (offset < 0) -1 else 1
            )
        }
    }
}

private fun startChild(child: View, delay: Long, translationY: Int) {
    child.alpha = 0f

    val alphaAnimator: Animator = ObjectAnimator.ofFloat(child, "alpha", 0.0f, 1.0f)
    alphaAnimator.duration = 200
    alphaAnimator.interpolator = AccelerateInterpolator()

    val translationAnimator: Animator =
        ObjectAnimator.ofFloat(child, "translationY", translationY.toFloat(), 0f)
    translationAnimator.duration = 275
    translationAnimator.interpolator = DecelerateInterpolator()

    val animatorSet = AnimatorSet()
    animatorSet.playTogether(alphaAnimator, translationAnimator)
    animatorSet.startDelay = delay
    animatorSet.start()
}

private fun getBounds(width: Int, height: Int, centerX: Int, centerY: Int): Array<Rect> {
    val endWidth = Math.max(centerX, width - centerX)
    val endHeight = Math.max(centerY, height - centerY)
    val endLeft = centerX - endWidth
    val endRight = centerX + endWidth
    val endTop = centerY - endHeight
    val endBottom = centerY + endHeight
    val end = Rect(endLeft, endTop, endRight, endBottom)
    val max = Rect(0, 0, width, height)

    return arrayOf(end, max)
}

private fun PropertyHolder.createBoundsAnimator(
    width: Int,
    height: Int,
    centerX: Int,
    centerY: Int,
    start: Rect?
): Animator {
    val speed = 4096
    val endWidth = max(centerX, width - centerX)
    val endHeight = max(centerY, height - centerY)
    val rect = getBounds(
        width,
        height,
        centerX,
        centerY
    )
    val end = rect[0]
    val max = rect[1]
    var duration = (max(endWidth, endHeight).toFloat() / speed * 1000).toLong()
    duration = max(duration, 150)
    duration = min(duration, 300)

    val animator: Animator = ObjectAnimator
        .ofObject(
            this,
            SimpleMenuBoundsProperty,
            RectEvaluatorCompat(
                max
            ),
            start,
            end
        )
    animator.interpolator = DecelerateInterpolator()
    animator.duration = duration
    return animator
}

private fun createElevationAnimator(view: View, elevation: Float): Animator? {
    val animator: Animator =
        ObjectAnimator.ofObject<View, Float>(
            view,
            View.TRANSLATION_Z,
            FloatEvaluator() as TypeEvaluator<Float>,
            -elevation,
            0f
        )
    animator.interpolator = FastOutSlowInInterpolator()
    return animator
}

private data class PropertyHolder(
    val background: FixedBoundsDrawable,
    val contentView: View
)

private object SimpleMenuBoundsProperty :
    Property<PropertyHolder, Rect>(Rect::class.java, "bounds") {

    override fun get(holder: PropertyHolder): Rect = holder.background.fixedBounds

    override fun set(holder: PropertyHolder, value: Rect) {
        holder.background.fixedBounds = value
        holder.contentView.clipBounds = value
    }
}