package com.africell.africell.ui
import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable

import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.africell.africell.R


class ImageProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val MAX_PROGRESS = 10000
    }


    private var progressImageDrawable: Drawable? = null


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ImageProgress, defStyleAttr, 0)

        a?.let {
            val backgroundImageRes = it.getResourceId(R.styleable.ImageProgress_backgroundImage, 0)
            addBackgroundImage(backgroundImageRes)

            val foregroundImageRes = it.getResourceId(R.styleable.ImageProgress_progressImage, 0)
            addForegroundImage(foregroundImageRes)

            val progress = it.getInteger(R.styleable.ImageProgress_currentProgress, 0)
            setProgress(progress.coerceAtLeast(0))
        }
        a?.recycle()
    }

    private fun addBackgroundImage(imageRes: Int) {
        val view = createImageView()

        view.setImageResource(imageRes)

        addView(view)
    }

     fun addForegroundImage(imageRes: Int) {
        val view = createImageView()

        progressImageDrawable = getDrawableFromResource(imageRes)?.let { ClipDrawable(it, Gravity.START, ClipDrawable.HORIZONTAL) }
        view.setImageDrawable(progressImageDrawable)

        addView(view)
    }

    private fun createImageView(): ImageView = ImageView(context)

    private fun getDrawableFromResource(res: Int): Drawable? = ContextCompat.getDrawable(context, res)


    fun setProgress(/*@IntRange(from = 0, to = 10000) */progress: Int) {
        progressImageDrawable?.level = progress
    }

    fun animateProgress(
          /*  @IntRange(from = 0, to = 10000)*/ fromProgress: Int,
          /*  @IntRange(from = 0, to = 10000)*/ toProgress: Int,
          /*  @IntRange(from = 0, to = 10000)*/ progressInterval: Int = 200,
            timeInterval: Long = 100L,
            startDelay: Long = 300L
    ) {
        setProgress(fromProgress)

        val progressRunnable: Runnable = object : Runnable {
            override fun run() {
                val level = progressImageDrawable?.level ?: MAX_PROGRESS

                if (level < toProgress) {
                    progressImageDrawable?.level = (level + progressInterval).coerceAtMost(toProgress)
                    postDelayed(this, timeInterval)
                }
            }
        }

        postDelayed(progressRunnable, startDelay)
    }


    override fun onDetachedFromWindow() {
        handler?.removeCallbacksAndMessages(null)
        super.onDetachedFromWindow()
    }
}
