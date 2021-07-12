package com.africell.africell.ui.button

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import java.lang.ref.WeakReference
import kotlin.math.roundToInt


class LoadingProgressButton : MaterialButton {

    enum class State {
        IDLE,
        IDLE_SUCCESS,
        IDLE_ERROR,
        IDLE_TO_PROGRESS,
        PROGRESS,
        PROGRESS_TO_IDLE
    }


    var state: State = State.IDLE
        internal set

    private var morphingAnimatorSet: AnimatorSet? = null
    private var animatedDrawable: CircularProgressDrawable? = null

    private var originalWidth: Int = 0
    private var originalHeight: Int = 0
    private var originalCornerRadius: Int = 0

    private val pendingJobs: MutableList<Pair<State, CharSequence?>> =
        mutableListOf()//TODO replace with Job object later


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)


    init {
        //...
    }


    //TODO add:
    //- attribute for single color for progress
    //- attribute for multiple colors for progress
    //- attribute for animation duration
    //- attribute for icon that can be set for: normal, success, failure
    //...
    //- new states to handle success and failure (maybe just success?)
    //...

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (state == State.PROGRESS) {
            canvas?.drawIndeterminateProgress()
        }
    }

    private fun Canvas.drawIndeterminateProgress() {
        if (animatedDrawable == null || animatedDrawable?.isRunning != true) {
            animatedDrawable = CircularProgressDrawable(this@LoadingProgressButton.context)
                .apply {
                    setStyle(CircularProgressDrawable.LARGE)
                    setColorSchemeColors(Color.WHITE, Color.BLACK)
                }

            val offset = (width - height) / 2

            val padding = Rect()
            background.getPadding(padding)

            val left = offset + 0/*paddingProgress.toInt()*/ + padding.bottom
            val right = width - offset - 0/*paddingProgress.toInt()*/ - padding.bottom
            val bottom = height - 0/*paddingProgress.toInt()*/ - padding.bottom
            val top = 0/*paddingProgress.toInt()*/ + padding.top

            animatedDrawable?.setBounds(left, top, right, bottom)
            animatedDrawable?.callback = this@LoadingProgressButton
            animatedDrawable?.start()
        } else {
            animatedDrawable?.draw(this)
            invalidate()
        }
    }


    fun showProgress() {
        when (state) {
            State.IDLE, State.IDLE_ERROR, State.IDLE_SUCCESS -> morphToProgress()
            State.IDLE_TO_PROGRESS, State.PROGRESS -> {
            }
            State.PROGRESS_TO_IDLE -> pendingJobs.add(State.PROGRESS to null)
        }
    }

    fun hideProgress(@StringRes textToShowRes: Int) {
        hideProgress(resources.getString(textToShowRes))
    }

    fun hideProgress(textToShow: CharSequence?) {
        when (state) {
            State.PROGRESS -> morphToIdle(textToShow)
            State.IDLE, State.IDLE_SUCCESS, State.IDLE_ERROR -> text =
                textToShow//TODO animate text change?
            State.IDLE_TO_PROGRESS -> pendingJobs.add(State.IDLE to textToShow)
            State.PROGRESS_TO_IDLE -> {
                //TODO change text here?
            }
        }
    }


    private fun morphToProgress() {
        val initialWidth = width
        originalWidth = initialWidth
        val initialHeight = height
        originalHeight = initialHeight

        val initialCornerRadius = cornerRadius
        originalCornerRadius = initialCornerRadius
        val finalCornerRadius = 1_000.dpToPx()

        state = State.IDLE_TO_PROGRESS
        text = null
        isClickable = false

        val toWidth = 70.dpToPx()
        val toHeight = 70.dpToPx()


        val cornerAnimation: ObjectAnimator = ObjectAnimator.ofInt(
            this,
            "cornerRadius",
            initialCornerRadius,
            finalCornerRadius
        )

        val widthAnimation = ValueAnimator.ofInt(initialWidth, toWidth - 10.dpToPx())
        widthAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.width = `val`
            setLayoutParams(layoutParams)
        }

        val heightAnimation = ValueAnimator.ofInt(initialHeight, toHeight)
        heightAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.height = `val`
            setLayoutParams(layoutParams)
        }

        val weakRef = WeakReference(this@LoadingProgressButton)
        morphingAnimatorSet = AnimatorSet().apply {
            duration = 300
            playTogether(cornerAnimation, widthAnimation, heightAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    weakRef.get()?.state = State.PROGRESS

                    val pendingJob = pendingJobs.firstOrNull { it.first == State.IDLE }
                    pendingJob?.let {
                        hideProgress(it.second)
                        pendingJobs.remove(it)
                    }
                }
            })
        }
        morphingAnimatorSet?.start()
    }

    private fun morphToIdle(textToShow: CharSequence?) {
        val initialWidth = width
        val initialHeight = height

        val initialCornerRadius = cornerRadius
        val finalCornerRadius = originalCornerRadius

        state = State.PROGRESS_TO_IDLE
        text = null
        isClickable = false

        val toWidth = originalWidth
        val toHeight = originalHeight


        val cornerAnimation: ObjectAnimator = ObjectAnimator.ofInt(
            this,
            "cornerRadius",
            initialCornerRadius,
            finalCornerRadius
        )

        val widthAnimation = ValueAnimator.ofInt(initialWidth, toWidth)
        widthAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.width = `val`
            setLayoutParams(layoutParams)
        }

        val heightAnimation = ValueAnimator.ofInt(initialHeight, toHeight)
        heightAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.height = `val`
            setLayoutParams(layoutParams)
        }

        val weakRef = WeakReference(this@LoadingProgressButton)
        morphingAnimatorSet = AnimatorSet().apply {
            duration = 300
            playTogether(cornerAnimation, widthAnimation, heightAnimation)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    weakRef.get()?.run {
                        state = State.IDLE
                        text = textToShow
                    }
                    isClickable = true

                    val pendingJob = pendingJobs.firstOrNull { it.first == State.PROGRESS }
                    pendingJob?.let {
                        showProgress()
                        pendingJobs.remove(it)
                    }
                }
            })
        }
        morphingAnimatorSet?.start()
    }


    fun cancelAnimations() {
        pendingJobs.clear()
        animatedDrawable?.stop()
        morphingAnimatorSet?.cancel()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimations()
    }


    private inline fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).roundToInt()
}