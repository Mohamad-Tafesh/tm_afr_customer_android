package com.tedmob.africell.ui.date

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.theme.overlay.MaterialThemeOverlay
import com.tedmob.africell.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateInput : TextInputLayout {

    companion object {
        const val DATE_PICKER_TAG = "DatePicker"
    }


    enum class DateType {
        DATE,
        //...
    }


    private var editTextStyle: Int? = null
    private var pickerFragment: DatePickerSpinnerFragment? = null//TODO take into account different DateType values
    private var formatter: DateFormat? = null


    private lateinit var colorStateList: ColorStateList

    /**
     * The view that will display the selected item.
     */
    private val editText: TextInputEditText by lazy {
        editTextStyle?.let { TextInputEditText(ContextThemeWrapper(context, it), null, 0) }
            ?: TextInputEditText(context)
    }


    constructor(context: Context) :
            super(
                MaterialThemeOverlay.wrap(context, null, R.attr.materialDateInputStyle, 0),
                null,
                R.attr.materialDateInputStyle
            ) {

        init(getContext(), null, R.attr.materialDateInputStyle)
    }

    constructor(context: Context, attrs: AttributeSet?) :
            super(
                MaterialThemeOverlay.wrap(context, attrs, R.attr.materialDateInputStyle, 0),
                attrs,
                R.attr.materialDateInputStyle
            ) {

        init(getContext(), attrs, R.attr.materialDateInputStyle)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, 0), attrs, defStyleAttr) {

        init(getContext(), attrs, defStyleAttr)
    }


    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.DateInput, defStyleAttr) {
            getResourceId(R.styleable.DateInput_contentStyle, 0).let {
                editTextStyle = if (it != 0) it else null
            }

            getInteger(R.styleable.DateInput_android_gravity, -1).let {
                if (it > -1) {
                    gravity = it
                    editText.gravity = it
                }
            }

            editText.isEnabled = getBoolean(R.styleable.DateInput_android_enabled, editText.isEnabled)

            getColorStateList(R.styleable.DateInput_android_textColor)?.let {
                editText.setTextColor(it)
            }
            getDimensionPixelSize(
                R.styleable.DateInput_android_textSize,
                -1
            ).takeIf { it > 0 }?.let {
                editText.textSize = it.toFloat()
            }
            getText(R.styleable.DateInput_android_text)?.let {
                // Allow text in debug mode for preview purposes.
                if (isInEditMode) {
                    editText.setText(it)
                } else {
                    throw RuntimeException("Don't set text directly. You probably want setDate instead.")
                }
            }

            val type = DateType.values()[getInt(R.styleable.DateInput_dateType, 0)]
            //TODO use type

            val pickerTitle = getString(R.styleable.DateInput_pickerTitle)
            val dateFormat = getString(R.styleable.DateInput_dateFormat)
            setupPicker(pickerTitle, dateFormat)

            formatter = SimpleDateFormat(dateFormat ?: "dd/MM/yyyy", Locale.ENGLISH)
        }

        this.addView(editText, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        isClickable = false
        isFocusable = false
        isFocusableInTouchMode = false

        editText.inputType = InputType.TYPE_NULL
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false
        editText.setOnClickListener { showDatePicker() }

        if (endIconMode == END_ICON_CLEAR_TEXT) {
            //overriding behavior of clear_text in order to clear the currentCalendar variable as well
            setEndIconOnClickListener {
                editText.text?.clear()
                currentCalendar = null
                //refreshEndIconDrawableState()//fixme: This line was added in version 1.3.0-alpha03 of Material Components.
            }
        }
    }

    //TODO take into account different DateType values
    private fun setupPicker(title: String?, format: String?) {
        pickerFragment = DatePickerSpinnerFragment.assemble {
            this.title = title
            format?.let { this.dateFormat = it }
        }

        pickerFragment?.onCalendarSetListener = {
            currentCalendar = it
            onDatePicked?.invoke(it)
        }
    }


    var fragmentManagerProvider: () -> FragmentManager = {
        TODO("You need to set the ${DateInput::class.java.simpleName}'s fragmentManagerProvider.")
    }
    var currentCalendar: Calendar? = null
        set(value) {
            field = value
            updateDate()
        }
    var onDatePicked: ((calendar: Calendar) -> Unit)? = null

    fun setDate(calendar: Calendar) {
        currentCalendar = calendar
    }

    fun setDateFormat(format: String) {
        formatter = SimpleDateFormat(format, Locale.ENGLISH)
        //TODO update Picker as well when option is available
    }


    private fun updateDate() {
        editText.setText(currentCalendar?.let { formatter?.format(it.time).orEmpty() })
    }


    private fun showDatePicker() {
        pickerFragment?.show(fragmentManagerProvider(), DATE_PICKER_TAG)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        fragmentManagerProvider = {
            TODO("You need to set the ${DateInput::class.java.simpleName}'s fragmentManagerProvider.")
        } //releasing any possible references to FragmentManagers.
    }
}