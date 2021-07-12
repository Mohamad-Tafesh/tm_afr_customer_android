package com.africell.africell.ui.blocks

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.africell.africell.R

class LoadingView : FrameLayout {

    val viewSwitcher: ViewSwitcher
    val imageView: ImageView
    val textView: TextView
    val button: Button
    val loadingFrame: FrameLayout

    var isLoading = true
        private set

    // message
    private var message: String?
    private var displayMessage: Boolean
    private var messageColor: Int = Color.BLACK

    // button
    private var buttonText = ""
    private var displayButton = true
    private var buttonClickListener: OnClickListener? = null

    // image
    private var displayImage = false
    private var image: Drawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.loadingViewStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {

        inflate(getContext(), R.layout.blocks_loading_view, this)
        viewSwitcher = findViewById(R.id.loading_view_switcher)
        imageView = findViewById(R.id.loading_view_image)
        textView = findViewById(R.id.loading_view_message)
        button = findViewById(R.id.loading_view_button)
        loadingFrame = findViewById(R.id.loading_view_loading_frame)


        val theme = context.theme
        val a = theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingView,
            defStyleAttr,
            R.style.Blocks_LoadingView
        )

        isLoading = a.getBoolean(R.styleable.LoadingView_isLoading, true)
        message = a.getString(R.styleable.LoadingView_message)
        displayMessage = a.getBoolean(R.styleable.LoadingView_displayMessage, true)
        messageColor = a.getColor(R.styleable.LoadingView_messageTextColor, 1)
        if (messageColor == 1) {
            val messageColorRes =
                a.getResourceId(R.styleable.LoadingView_messageTextColor, android.R.color.black)
            messageColor = ContextCompat.getColor(context, messageColorRes)
        }


        buttonText = a.getString(R.styleable.LoadingView_buttonText) ?: buttonText
        displayButton = a.getBoolean(R.styleable.LoadingView_displayButton, true)

        displayImage = a.getBoolean(R.styleable.LoadingView_displayImage, false)
        image = a.getDrawable(R.styleable.LoadingView_image)

        val loadingFrameLayout = a.getResourceId(
            R.styleable.LoadingView_loadingFrameLayout,
            R.layout.loading_frame_default
        )

        a.recycle()

        loadingFrame(loadingFrameLayout)
        loading(isLoading)
        updateImage()
        displayImage(displayImage)
        message?.let { message(it) }
        messageColor(messageColor)
        displayMessage(displayMessage)
        buttonText(buttonText)
        buttonClickListener(buttonClickListener)
        displayButton(displayButton)
    }

    private fun updateImage() {
        imageView.setImageDrawable(image)
    }

    fun imageResource(@DrawableRes resId: Int): LoadingView {
        image = ContextCompat.getDrawable(context, resId)
        updateImage()
        return this
    }

    fun imageBitmap(bitmap: Bitmap): LoadingView {
        image = BitmapDrawable(resources, bitmap)
        updateImage()
        return this
    }

    fun message(message: String): LoadingView {
        this.message = message
        textView.text = message
        return this
    }

    fun buttonText(buttonText: String): LoadingView {
        this.buttonText = buttonText
        button.text = buttonText
        return this
    }

    fun messageColor(messageColor: Int): LoadingView {
        this.messageColor = messageColor
        textView.setTextColor(messageColor)
        return this
    }

    fun buttonClickListener(listener: OnClickListener?): LoadingView {
        this.buttonClickListener = listener
        button.setOnClickListener(listener)
        return this
    }

    fun loading(loading: Boolean): LoadingView {
        this.isLoading = loading
        viewSwitcher.displayedChild = if (isLoading) 0 else 1
        return this
    }

    fun displayMessage(displayMessage: Boolean): LoadingView {
        this.displayMessage = displayMessage
        textView.visibility = if (displayMessage) View.VISIBLE else View.GONE
        return this
    }

    fun displayButton(displayButton: Boolean): LoadingView {
        this.displayButton = displayButton
        button.visibility = if (displayButton) View.VISIBLE else View.GONE
        return this
    }

    fun displayImage(displayImage: Boolean): LoadingView {
        this.displayImage = displayImage
        imageView.visibility = if (displayImage) View.VISIBLE else View.GONE
        return this
    }

    fun loadingFrame(view: View) {
        loadingFrame.removeAllViews()
        loadingFrame.addView(view)
        requestLayout()
        invalidate()
    }

    fun loadingFrame(@LayoutRes layoutRes: Int) {
        val v = LayoutInflater.from(loadingFrame.context)
            .inflate(layoutRes, loadingFrame, false)
        loadingFrame(v)
    }
}
