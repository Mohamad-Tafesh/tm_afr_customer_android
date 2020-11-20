package com.tedmob.africell.ui.date

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tedmob.africell.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DatePickerSpinnerFragment : DialogFragment() {

    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_YEAR = "year"
        private const val KEY_MONTH = "month"
        private const val KEY_DAY = "day"
        private const val KEY_CLEAR_BUTTON = "clear_button"
        private const val KEY_DATE_FORMAT = "date_format"

        @JvmOverloads
        fun newInstance(
            title: String? = null,
            date: Long? = null,
            showClearButton: Boolean = false,
            dateFormat: String = "MMM d, yyyy"
        ): DatePickerSpinnerFragment {
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.timeInMillis = date
            }

            return DatePickerSpinnerFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title,
                    KEY_YEAR to calendar.get(Calendar.YEAR),
                    KEY_MONTH to calendar.get(Calendar.MONTH),
                    KEY_DAY to calendar.get(Calendar.DAY_OF_MONTH),
                    KEY_CLEAR_BUTTON to showClearButton,
                    KEY_DATE_FORMAT to dateFormat
                )
            }
        }

        inline fun assemble(block: Builder.() -> Unit): DatePickerSpinnerFragment = Builder().apply(block).build()
    }

    class Builder {
        var title: String? = null
        var date: Long? = null
        var showClearButton: Boolean = false
        var dateFormat: String = "MMM d, yyyy"

        fun build(): DatePickerSpinnerFragment =
            newInstance(title, date, showClearButton, dateFormat)
    }


    var onDateSetListener: ((year: Int, month: Int, day: Int) -> Unit)? = null
    var onCalendarSetListener: ((calendar: Calendar) -> Unit)? = null
    var onClearDateListener: (() -> Unit)? = null

    var minDate: Long? = null
        set(value) {
            field = value
            datePicker?.minDate = value?.let { Calendar.getInstance().apply { timeInMillis = it } }
        }
    var maxDate: Long? = null
        set(value) {
            field = value
            datePicker?.maxDate = value?.let { Calendar.getInstance().apply { timeInMillis = it } }
        }

    private var datePicker: DateNumbersPicker? = null

    private val dateFormatter: DateFormat by lazy {
        SimpleDateFormat(arguments?.getString(KEY_DATE_FORMAT) ?: "", Locale.ENGLISH)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments//to reduce number of field accesses
        val newTitle = args?.getString(KEY_TITLE)
        val year = args?.getInt(KEY_YEAR) ?: 1970
        val month = (args?.getInt(KEY_MONTH) ?: 0) + 1
        val day = args?.getInt(KEY_DAY) ?: 1
        val addClearButton = args?.getBoolean(KEY_CLEAR_BUTTON) ?: false

        val view = /*LayoutInflater.from(requireContext())*/
            layoutInflater.inflate(R.layout.date_picker_spinner_numbers, FrameLayout(requireContext()), false)
        datePicker = DateNumbersPicker(view)

        val dialog = MaterialAlertDialogBuilder(requireActivity(), R.style.DatePickerSpinnerDialogTheme)
            .setTitle(newTitle ?: getString(R.string.dp_s__select_date))
            .setView(view)
            .setPositiveButton(R.string.dp_s__ok) { _, _ ->
                val calendar = Calendar.getInstance().apply {
                    clear()
                    set(Calendar.YEAR, datePicker?.yearPicker?.value ?: 0)
                    set(Calendar.MONTH, datePicker?.monthPicker?.value?.minus(1) ?: 0)
                    set(Calendar.DAY_OF_MONTH, datePicker?.dayPicker?.value ?: 0)
                }

                onDateSetListener?.invoke(
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
                )
                onCalendarSetListener?.invoke(calendar)
            }
            .setNegativeButton(R.string.dp_s__cancel, null)
            .also {
                if (addClearButton) {
                    it.setNeutralButton(R.string.dp_s__clear) { _, _ ->
                        onClearDateListener?.invoke()
                    }
                }
            }
            .create()

        datePicker?.dateFormatter = dateFormatter
        datePicker?.setDate(year, month, day)
        datePicker?.minDate = minDate?.let { Calendar.getInstance().apply { timeInMillis = it } }
        datePicker?.maxDate = maxDate?.let { Calendar.getInstance().apply { timeInMillis = it } }

        return dialog
    }


    private class DateNumbersPicker(private val view: View) {
        val yearPicker: NumberPicker = view.findViewById(R.id.yearPicker)
        val monthPicker: NumberPicker = view.findViewById(R.id.monthPicker)
        val dayPicker: NumberPicker = view.findViewById(R.id.dayPicker)
        val dateText: TextView = view.findViewById(R.id.dateText)

        var minDate: Calendar? = null
            set(value) {
                field = value
                setNewBoundaries()
            }
        var maxDate: Calendar? = null
            set(value) {
                field = value
                setNewBoundaries()
            }

        var dateFormatter: DateFormat? = null

        private var currentCalendar: Calendar = Calendar.getInstance()


        companion object {
            private val maxDaysPerMonthMap: Map<Int, Int> = mapOf(
                1 to 31,
                2 to 28,
                3 to 31,
                4 to 30,
                5 to 31,
                6 to 30,
                7 to 31,
                8 to 31,
                9 to 30,
                10 to 31,
                11 to 30,
                12 to 31
            )
            private val displayedMonthValues = arrayOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )
        }


        init {
            yearPicker.minValue = 1
            monthPicker.minValue = 1
            dayPicker.minValue = 0

            monthPicker.displayedValues = displayedMonthValues

            yearPicker.maxValue = Int.MAX_VALUE

            //Note: change listeners are not called when value changes due to changing boundaries (minDate or maxDate).

            yearPicker.setOnValueChangedListener { _, _, newVal ->
                //update maxDays for February:
                val month = monthPicker.value
                if (month == 2) {
                    val maxDays = maxDaysPerMonthMap[2] ?: 0
                    dayPicker.maxValue = if (newVal % 4 == 0) maxDays + 1 else maxDays
                } else {
                    //updating max days here to take into account change of boundaries (year is at bound --> year no longer at bound)
                    dayPicker.maxValue = maxDaysPerMonthMap[month] ?: 0
                }

                updateDate()
            }

            monthPicker.setOnValueChangedListener { _, _, newVal ->
                val maxDays = maxDaysPerMonthMap[newVal] ?: 0
                dayPicker.maxValue = if ((newVal == 2) && (yearPicker.value % 4 == 0)) maxDays + 1 else maxDays

                updateDate()
            }

            dayPicker.setOnValueChangedListener { _, _, newVal ->
                updateDate()
            }
        }

        fun setDate(year: Int, month: Int, day: Int) {
            yearPicker.value = year

            monthPicker.maxValue = 12//initial condition
            monthPicker.value = month

            val shouldAddDay = (month == 2) && (year % 4 == 0)

            val maxDays = maxDaysPerMonthMap[month] ?: 0
            dayPicker.maxValue = if (shouldAddDay) maxDays + 1 else maxDays//initial condition
            dayPicker.value = day

            updateDate()
        }


        private fun setDateText(year: Int, month: Int, day: Int) {
            val date = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, day)
            }
            dateText.text = dateFormatter?.format(date.time)
        }

        private fun updateDate() {
            currentCalendar[Calendar.YEAR] = yearPicker.value
            currentCalendar[Calendar.MONTH] = monthPicker.value - 1
            currentCalendar[Calendar.DAY_OF_MONTH] = dayPicker.value

            minDate?.let {
                if (currentCalendar[Calendar.YEAR] == it[Calendar.YEAR]) {
                    monthPicker.minValue = it[Calendar.MONTH] + 1

                    if (currentCalendar[Calendar.MONTH] == it[Calendar.MONTH]) {
                        dayPicker.minValue = it[Calendar.DAY_OF_MONTH]
                    } else {
                        dayPicker.minValue = 1
                    }
                } else {
                    monthPicker.minValue = 1
                    dayPicker.minValue = 1
                }
                //updating values in case they were changed from boundaries check
                currentCalendar[Calendar.MONTH] = monthPicker.value - 1
                currentCalendar[Calendar.DAY_OF_MONTH] = dayPicker.value
            }

            maxDate?.let {
                if (currentCalendar[Calendar.YEAR] == it[Calendar.YEAR]) {
                    monthPicker.maxValue = it[Calendar.MONTH] + 1

                    if (monthPicker.value - 1 == it[Calendar.MONTH]) {//have to use new value of month immediately for accurate update
                        dayPicker.maxValue = it[Calendar.DAY_OF_MONTH]
                    }
                } else {
                    monthPicker.maxValue = 12
                }
                //updating values in case they were changed from boundaries check
                currentCalendar[Calendar.MONTH] = monthPicker.value - 1
                currentCalendar[Calendar.DAY_OF_MONTH] = dayPicker.value
            }

            setDateText(yearPicker.value, monthPicker.value, dayPicker.value)
        }

        private fun setNewBoundaries() {
            if (minDate != null) {
                minDate?.let {
                    val newDate = Calendar.getInstance().apply {
                        set(Calendar.YEAR, yearPicker.value)
                        set(Calendar.MONTH, monthPicker.value - 1)
                        set(Calendar.DAY_OF_MONTH, dayPicker.value)
                    }

                    yearPicker.minValue =
                        it[Calendar.YEAR]//shortcut since this will only change if minDate is changed itself

                    if (newDate.before(it)) {
                        setDate(it[Calendar.YEAR], it[Calendar.MONTH] + 1, it[Calendar.DAY_OF_MONTH])
                        return
                    }

                    updateDate()
                }
            } else {
                //reset min to defaults
                yearPicker.minValue = 1
                monthPicker.minValue = 1
                dayPicker.minValue = 0
            }

            if (maxDate != null) {
                maxDate?.let {
                    val newDate = Calendar.getInstance().apply {
                        set(Calendar.YEAR, yearPicker.value)
                        set(Calendar.MONTH, monthPicker.value - 1)
                        set(Calendar.DAY_OF_MONTH, dayPicker.value)
                    }

                    yearPicker.maxValue =
                        it[Calendar.YEAR]//shortcut since this will only change if maxDate is changed itself

                    if (newDate.after(it)) {
                        setDate(it[Calendar.YEAR], it[Calendar.MONTH] + 1, it[Calendar.DAY_OF_MONTH])
                        return
                    }

                    updateDate()
                }
            } else {
                //reset max to defaults (day is dependent on month selected)
                yearPicker.maxValue = Int.MAX_VALUE
                monthPicker.maxValue = 12

                val shouldAddDay = (monthPicker.value == 2) && (yearPicker.value % 4 == 0)
                val maxDays = maxDaysPerMonthMap[12] ?: 0
                dayPicker.maxValue = if (shouldAddDay) maxDays + 1 else maxDays
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        datePicker = null
    }
}


inline fun DatePickerSpinnerFragment.disableFuture(): DatePickerSpinnerFragment =
    apply { maxDate = Calendar.getInstance().timeInMillis }

inline fun DatePickerSpinnerFragment.disablePast(): DatePickerSpinnerFragment =
    apply { minDate = Calendar.getInstance().timeInMillis }

inline fun DatePickerSpinnerFragment.withRange(min: Long?, max: Long?): DatePickerSpinnerFragment =
    apply { this.minDate = min; this.maxDate = max }