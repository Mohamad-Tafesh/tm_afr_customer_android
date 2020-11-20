package com.tedmob.africell.util


import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.tedmob.africell.R
import java.util.*


class DatePickerFragment : androidx.fragment.app.DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var onDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var onClearDateListener: OnClearDateListener? = null

    private var minDate: Long = 0
    private var maxDate: Long = 0

    interface OnClearDateListener {
        fun onClearDate(view: DatePicker)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments
        val year = args?.getInt("year") ?: 1970
        val month = args?.getInt("month") ?: 1
        val day = args?.getInt("day") ?: 1
        val showCalendar = args?.getBoolean("show_calendar") ?: true
        val addClearButton = args?.getBoolean("clear_button") ?: false


        val dialog: DatePickerDialog

        if (showCalendar) {
            dialog = DatePickerDialog(requireContext(), this, year, month, day)
        } else {
            dialog = DatePickerDialog(requireContext(), R.style.AppTheme_DialogSpinnerMode, this, year, month, day)
        }

        if (addClearButton) {
            val context = context

            dialog.setButton(
                DialogInterface.BUTTON_POSITIVE, context?.getText(android.R.string.ok)
            ) { _, _ ->
                onDateSet(
                    dialog.datePicker, dialog.datePicker.year,
                    dialog.datePicker.month, dialog.datePicker.dayOfMonth
                )
            }

            dialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, context?.getText(android.R.string.cancel)
            ) { _, _ -> }

            dialog.setButton(
                DialogInterface.BUTTON_NEUTRAL, context?.getText(R.string.clear)
            ) { _, _ ->
                if (onClearDateListener != null) {
                    onClearDateListener?.onClearDate(dialog.datePicker)
                }
            }
        }
        if (minDate != 0L) {
            dialog.datePicker.minDate = minDate
        }
        if (maxDate != 0L) {
            dialog.datePicker.maxDate = maxDate
        }
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
        }
        return dialog


    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        if (onDateSetListener != null) {
            onDateSetListener?.onDateSet(view, year, month, day)
        }
    }

    fun setOnDateSetListener(onDateSetListener: DatePickerDialog.OnDateSetListener) {
        this.onDateSetListener = onDateSetListener
    }

    fun setOnClearDateListener(onClearDateListener: OnClearDateListener) {
        this.onClearDateListener = onClearDateListener
    }

    fun setMinDate(minDate: Long) {
        this.minDate = minDate
    }

    fun disablePast() {
        this.minDate = Calendar.getInstance().timeInMillis
    }

    fun disableFuture() {
        this.maxDate = Calendar.getInstance().timeInMillis
    }
    fun setMaxDate(maxDate: Long) {
        this.maxDate = maxDate
    }
    fun setAge18() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18)
        this.maxDate = calendar.timeInMillis
    }

    fun setMinimumDate( field:Int,  amount:Int){
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(field, amount)
        this.setMinDate(calendar.timeInMillis)

    }

    fun setMaximumDate(field:Int,  amount:Int){
        val calendar1 = Calendar.getInstance()
        calendar1.time = Date()
        calendar1.add(field, amount)
        setMaxDate(calendar1.timeInMillis)
    }



    companion object {

        @JvmOverloads
        fun newInstance(
            date: Long? = null,
            showCalendar: Boolean = false,
            showClearButton: Boolean = false
        ): DatePickerFragment {
            val fragment = DatePickerFragment()
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.timeInMillis = date
            }
            val args = Bundle()
            args.putInt("year", calendar.get(Calendar.YEAR))
            args.putInt("month", calendar.get(Calendar.MONTH))
            args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH))
            args.putBoolean("show_calendar", showCalendar)
            args.putBoolean("clear_button", showClearButton)

            fragment.arguments = args
            return fragment
        }
    }
}
