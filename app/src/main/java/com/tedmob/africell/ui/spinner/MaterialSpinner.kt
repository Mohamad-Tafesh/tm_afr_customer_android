package com.tedmob.africell.ui.spinner

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.content.res.Resources
import android.database.DataSetObserver
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.SoundEffectConstants
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.accessibility.AccessibilityEvent
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.customview.view.AbsSavedState
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tedmob.africell.R
import com.tedmob.africell.ui.spinner.searchable.MaterialSearchableListDialogHolder
import com.tedmob.africell.ui.spinner.searchable.MaterialSearchableSpinnerItem
import java.util.*
import kotlin.math.roundToInt
import androidx.appcompat.widget.ThemedSpinnerAdapter as ThemedSpinnerAdapterCompat

//Source: https://github.com/tiper/MaterialSpinner
@SuppressLint("RestrictedApi")//Until extracting PorterDuff.Mode is not just for Android team
open class MaterialSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.materialSpinnerStyle,//com.google.android.material.R.attr.textInputStyle,
    mode: Int = MODE_DROPDOWN
) : TextInputLayout(context/*super(wrap(context, attrs, defStyleAttr, 0))*/, attrs, defStyleAttr) {

    companion object {
        /**
         * Represents an invalid position.
         * All valid positions are in the range 0 to 1 less than the number of items in the current adapter.
         */
        const val INVALID_POSITION = -1

        /**
         * Use a dialog window for selecting spinner options.
         */
        const val MODE_DIALOG = 0

        /**
         * Use a dropdown anchored to the Spinner for selecting spinner options.
         */
        const val MODE_DROPDOWN = 1

        /**
         * Use a bottom sheet dialog window for selecting spinner options.
         */
        const val MODE_BOTTOMSHEET = 2

        /**
         * Use a dialog window for selecting spinner options with search options.
         */
        const val MODE_DIALOG_SEARCHABLE = 3

        //TODO Alfred.abdo: since TextInputLayout is a vertical LinearLayout, maybe add an option to inflate the items below the
        // TextInputEditText; this would need an expandable-like layout implementation (including proper animations).

        //TODO Alfred.abdo: allow for custom mode, where the popup is provided by the user of the library.
    }


    //constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)


    private val colorStateList: ColorStateList

    /**
     * The view that will display the available list of choices.
     */
    private val popup: SpinnerPopup

    private var editTextStyle: Int? = null

    /**
     * The view that will display the selected item.
     */
    private val editText: TextInputEditText by lazy {
        editTextStyle?.let { TextInputEditText(ContextThemeWrapper(context, it), null, 0) }
            ?: TextInputEditText(getContext())
    }

    /**
     * Extended [android.widget.Adapter] that is the bridge between this Spinner and its data.
     */
    var adapter: SpinnerAdapter? = null
        set(value) {
            field = DropDownAdapter(value, context.theme).also { popup.setAdapter(it) }
        }

    /**
     * The listener that receives notifications when an item is selected.
     */
    var onItemSelectedListener: OnItemSelectedListener? = null

    /**
     * The listener that receives notifications when an item is clicked.
     */
    var onItemClickListener: OnItemClickListener? = null

    /**
     * The layout direction of this view.
     * {@link #LAYOUT_DIRECTION_RTL} if the layout direction is RTL.
     * {@link #LAYOUT_DIRECTION_LTR} if the layout direction is not RTL.
     */
    private var direction =
        if (isLayoutRtl()) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR

    /**
     * The currently selected item.
     */
    var selection = INVALID_POSITION
        set(value) {
            field = value
            adapter?.apply {
                if (value in 0 until count) {
                    editText.setText(
                        when (val item = getItem(value) ?: "") {
                            is CharSequence -> item
                            is MaterialSpinnerItem -> item.toString()
                            else -> item.toString()
                        }
                    )
                    onItemSelectedListener?.onItemSelected(
                        this@MaterialSpinner,
                        null,
                        value,
                        getItemId(value)
                    )
                } else {
                    editText.setText("")
                    onItemSelectedListener?.onNothingSelected(this@MaterialSpinner)
                }
            }
        }

    /**
     * Sets the [prompt] to display when the dialog is shown.
     *
     * @return The prompt to display when the dialog is shown.
     */
    var prompt: CharSequence?
        set(value) {
            popup.setPromptText(value)
        }
        get() = popup.getPrompt()

    /**
     * @return The data corresponding to the currently selected item, or null if there is nothing
     * selected.
     */
    val selectedItem: Any?
        get() = popup.getItem(selection)

    /**
     * @return The id corresponding to the currently selected item, or {@link #INVALID_ROW_ID} if
     * nothing is selected.
     */
    val selectedItemId: Long
        get() = popup.getItemId(selection)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MaterialSpinner, defStyleAttr, 0).run {
            getResourceId(R.styleable.MaterialSpinner_contentStyle, 0).let {
                editTextStyle = if (it != 0) it else null
            }

            getInteger(R.styleable.MaterialSpinner_android_gravity, -1).let {
                if (it > -1) {
                    gravity = it
                    editText.gravity = it
                }
            }

            editText.isEnabled =
                getBoolean(R.styleable.MaterialSpinner_android_enabled, editText.isEnabled)
            editText.isFocusable =
                getBoolean(R.styleable.MaterialSpinner_android_focusable, editText.isFocusable)
            editText.isFocusableInTouchMode =
                getBoolean(
                    R.styleable.MaterialSpinner_android_focusableInTouchMode,
                    editText.isFocusableInTouchMode
                )

            getColorStateList(R.styleable.MaterialSpinner_android_textColor)?.let {
                editText.setTextColor(it)
            }
            getDimensionPixelSize(
                R.styleable.MaterialSpinner_android_textSize,
                -1
            ).takeIf { it > 0 }?.let {
                editText.textSize = it.toFloat()
            }
            getText(R.styleable.MaterialSpinner_android_text)?.let {
                // Allow text in debug mode for preview purposes.
                if (isInEditMode) {
                    editText.setText(it)
                } else {
                    throw RuntimeException("Don't set text directly. You probably want setSelection instead.")
                }
            }

            popup = when (getInt(R.styleable.MaterialSpinner_spinnerMode, mode)) {
                MODE_DIALOG -> DialogPopup(context, getString(R.styleable.MaterialSpinner_prompt))
                MODE_BOTTOMSHEET -> BottomSheetPopup(
                    context,
                    getString(R.styleable.MaterialSpinner_prompt)
                )
                MODE_DIALOG_SEARCHABLE -> SearchableDialogPopup(context, getString(R.styleable.MaterialSpinner_prompt))
                else -> DropdownPopup(context, attrs, getString(R.styleable.MaterialSpinner_prompt))
            }

            // Create the color state list.
            //noinspection Recycle
            colorStateList = context.obtainStyledAttributes(
                attrs,
                intArrayOf(R.attr.colorControlActivated, R.attr.colorControlNormal)
            ).run {
                val activated = getColor(0, 0)

                @SuppressLint("ResourceType")
                val normal = getColor(1, 0)
                recycle()

                ColorStateList(
                    arrayOf(
                        intArrayOf(android.R.attr.state_pressed),
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf()
                    ), intArrayOf(activated, activated, normal)
                )
            }

            // Set the arrow and properly tint it.
            val tintMode = getInteger(
                R.styleable.MaterialSpinner_android_drawableTintMode,
                getInteger(R.styleable.MaterialSpinner_drawableTintMode, -1)
            )

            recycle()
        }

        this.addView(editText, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        popup.setOnDismissListener(
            object : SpinnerPopup.OnDismissListener {
                override fun onDismiss() {
                    //editText.clearFocus()
                }
            }
        )

        // Disable input.
        editText.maxLines = 1
        editText.inputType = InputType.TYPE_NULL

        editText.setOnClickListener { popup.show(selection) }

        editText.onFocusChangeListener.let {
            editText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                v.handler?.post {
                    if (hasFocus) {
                        v.performClick()
                    }
                    it?.onFocusChange(v, hasFocus)
                    onFocusChangeListener?.onFocusChange(this, hasFocus)
                }
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw RuntimeException("Don't call setOnClickListener. You probably want setOnItemClickListener instead.")
    }

    /**
     * Set whether this view can receive the focus.
     * Setting this to false will also ensure that this view is not focusable in touch mode.
     *
     * @param [focusable] If true, this view can receive the focus.
     *
     * @see [android.view.View.setFocusableInTouchMode]
     * @see [android.view.View.setFocusable]
     * @attr ref android.R.styleable#View_focusable
     */
    override fun setFocusable(focusable: Boolean) {
        editText.isFocusable = focusable
        super.setFocusable(focusable)
    }

    /**
     * Set whether this view can receive focus while in touch mode.
     * Setting this to true will also ensure that this view is focusable.
     *
     * @param [focusableInTouchMode] If true, this view can receive the focus while in touch mode.
     *
     * @see [android.view.View.setFocusable]
     * @attr ref android.R.styleable#View_focusableInTouchMode
     */
    override fun setFocusableInTouchMode(focusableInTouchMode: Boolean) {
        editText.isFocusableInTouchMode = focusableInTouchMode
        super.setFocusableInTouchMode(focusableInTouchMode)
    }

    /**
     * @see [android.view.View.onRtlPropertiesChanged]
     */
    override fun onRtlPropertiesChanged(layoutDirection: Int) {
        if (direction != layoutDirection) {
            direction = layoutDirection
            editText.compoundDrawables.let {
                editText.setCompoundDrawablesWithIntrinsicBounds(it[2], null, it[0], null)
            }
        }
        super.onRtlPropertiesChanged(layoutDirection)
    }

    /**
     * Call the OnItemClickListener, if it is defined.
     * Performs all normal actions associated with clicking: reporting accessibility event, playing a sound, etc.
     *
     * @param [view] The view within the adapter that was clicked.
     * @param [position] The position of the view in the adapter.
     * @param [id] The row id of the item that was clicked.
     * @return True if there was an assigned OnItemClickListener that was called, false otherwise is returned.
     */
    fun performItemClick(view: View?, position: Int, id: Long): Boolean =
        run {
            onItemClickListener?.let {
                playSoundEffect(SoundEffectConstants.CLICK)
                it.onItemClick(this@MaterialSpinner, view, position, id)
                true
            } ?: false
        }.also {
            view?.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED)
        }

    /**
     * Sets the prompt to display when the dialog is shown.
     *
     * @param [promptResId] the resource ID of the prompt to display when the dialog is shown.
     */
    fun setPromptResId(@StringRes promptResId: Int) {
        prompt = context.getText(promptResId)
    }

    override fun onSaveInstanceState(): Parcelable =
        SavedState(super.onSaveInstanceState()!!)
            .apply {
                this.selection = this@MaterialSpinner.selection
                this.isShowingPopup = this@MaterialSpinner.popup.isShowing()
            }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                selection = state.selection
                if (state.isShowingPopup) {
                    viewTreeObserver?.addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                if (!popup.isShowing()) {
                                    requestFocus()
                                }
                                viewTreeObserver?.removeOnGlobalLayoutListener(this)
                            }
                        }
                    )
                }
            }
            else -> super.onRestoreInstanceState(state)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (popup.isShowing()) {
            popup.dismiss()
        }
    }

    /**
     * Returns if this view layout should be in a RTL direction.
     * @return True if is RTL, false otherwise.
     */
    private inline fun isLayoutRtl(): Boolean = Locale.getDefault().isLayoutRtl()

    /**
     * Returns if this Locale direction is RTL.
     * @return True if is RTL, false otherwise.
     */
    private inline fun Locale.isLayoutRtl(): Boolean =
        TextUtils.getLayoutDirectionFromLocale(this) == View.LAYOUT_DIRECTION_RTL

    /**
     * @see [android.support.v4.content.res.ResourcesCompat.getDrawable]
     */
    private fun Context.getDrawableCompat(
        @DrawableRes id: Int,
        theme: Resources.Theme?
    ): Drawable? = resources.getDrawableCompat(id, theme)

    /**
     * @see [android.support.v4.content.res.ResourcesCompat.getDrawable]
     */
    @Throws(Resources.NotFoundException::class)
    private fun Resources.getDrawableCompat(
        @DrawableRes id: Int,
        theme: Resources.Theme?
    ): Drawable? =
        ResourcesCompat.getDrawable(this, id, theme)
            ?.let { DrawableCompat.wrap(it) }


    private inner class DialogPopup(
        val context: Context,
        private var prompt: CharSequence? = null
    ) : DialogInterface.OnClickListener, SpinnerPopup {

        private var popup: AlertDialog? = null
        private var adapter: ListAdapter? = null
        private var listener: SpinnerPopup.OnDismissListener? = null

        override fun setAdapter(adapter: ListAdapter?) {
            this.adapter = adapter
        }

        override fun setPromptText(hintText: CharSequence?) {
            prompt = hintText
        }

        override fun getPrompt(): CharSequence? = prompt

        override fun show(position: Int) {
            if (adapter == null) {
                return
            }

            popup = adapter?.let { adapter ->
                AlertDialog.Builder(context).let { builder ->
                    prompt?.let { builder.setTitle(it) }
                    builder.setSingleChoiceItems(adapter, position, this)
                        .create()
                        .apply {
                            popup?.listView?.let {
                                it.textDirection = textDirection
                                it.textAlignment = textAlignment
                            }
                            setOnDismissListener { listener?.onDismiss() }
                        }
                }.also {
                    it.show()
                }
            }
        }

        override fun dismiss() {
            popup?.dismiss()
        }

        override fun onClick(dialog: DialogInterface, which: Int) {
            this@MaterialSpinner.selection = which
            onItemClickListener?.let {
                this@MaterialSpinner.performItemClick(null, which, adapter?.getItemId(which) ?: 0L)
            }
            popup?.dismiss()
        }

        override fun setOnDismissListener(listener: SpinnerPopup.OnDismissListener?) {
            this.listener = listener
        }

        override fun getItem(position: Int): Any? = adapter?.getItem(position)

        override fun getItemId(position: Int): Long =
            adapter?.getItemId(position) ?: INVALID_POSITION.toLong()

        override fun isShowing() = popup?.isShowing == true
    }

    private inner class SearchableDialogPopup(
        val context: Context,
        private var prompt: CharSequence? = null
    ) : MaterialSearchableListDialogHolder.SearchItemClickListener, SpinnerPopup {

        private var popup: AlertDialog? = null
        private var adapter: ListAdapter? = null
        private var listener: SpinnerPopup.OnDismissListener? = null

        override fun setAdapter(adapter: ListAdapter?) {
            this.adapter = adapter
        }

        override fun setPromptText(hintText: CharSequence?) {
            prompt = hintText
        }

        override fun getPrompt(): CharSequence? = prompt

        override fun show(position: Int) {
            if (adapter == null) {
                return
            }

            popup = adapter?.let { adapter ->
                MaterialSearchableListDialogHolder.Builder(context).let { builder ->
                    prompt?.let { builder.setTitle(it) }
                    builder.setPositiveButton(

                        MaterialSearchableListDialogHolder.DEFAULT_CLOSE_STR,
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() }
                    )
                        .setOnSearchableItemClickListener(this)
                        .setOnDismissListener(DialogInterface.OnDismissListener { listener?.onDismiss() })
                        .create()
                }.let { dialog ->
                    dialog.setFromAdapter(adapter)
                    dialog.show()
                }
            }
        }

        override fun dismiss() {
            popup?.dismiss()
        }

        override fun onSearchableItemClicked(item: MaterialSearchableSpinnerItem, position: Int) {
            this@MaterialSpinner.selection = position
            onItemClickListener?.let {
                this@MaterialSpinner.performItemClick(null, position, adapter?.getItemId(position) ?: 0L)
            }
            popup?.dismiss()
        }

        override fun setOnDismissListener(listener: SpinnerPopup.OnDismissListener?) {
            this.listener = listener
        }

        override fun getItem(position: Int): Any? = adapter?.getItem(position)

        override fun getItemId(position: Int): Long =
            adapter?.getItemId(position) ?: INVALID_POSITION.toLong()

        override fun isShowing() = popup?.isShowing == true
    }

    /**
     * A PopupWindow that anchors itself to a host view and displays a list of choices.
     */
    @SuppressLint("RestrictedApi")
    private inner class DropdownPopup(
        context: Context,
        attrs: AttributeSet?,
        private var prompt: CharSequence? = null
    ) : ListPopupWindow(context, attrs), SpinnerPopup {

        private var internalPromptView: TextView? = null
        private var promptViewChanged: Boolean = false


        init {
            inputMethodMode = INPUT_METHOD_NOT_NEEDED
            anchorView = this@MaterialSpinner
            isModal = true

            promptPosition = POSITION_PROMPT_ABOVE
            setupPromptView(prompt)
            setPromptView(internalPromptView)

            setOverlapAnchor(false)

            setOnItemClickListener { _, v, position, id ->
                this@MaterialSpinner.selection = position
                if (onItemClickListener != null) {//Fixme Alfred.abdo: should perform click regardless?
                    this@MaterialSpinner.performItemClick(v, position, id)
                }
                dismiss()
            }
        }


        override fun show(position: Int) {
            if (promptViewChanged) {
                setPromptView(internalPromptView)
            }

            super.show()

            listView?.let {
                it.choiceMode = ListView.CHOICE_MODE_SINGLE
                it.textDirection = textDirection
                it.textAlignment = textAlignment
            }

            setSelection(position)
        }

        override fun setOnDismissListener(listener: SpinnerPopup.OnDismissListener?) {
            super.setOnDismissListener { listener?.onDismiss() }
        }

        override fun setPromptText(hintText: CharSequence?) {
            prompt = hintText
            setupPromptView(hintText)
            promptViewChanged = true
        }

        override fun getPrompt(): CharSequence? = prompt

        override fun getItem(position: Int): Any? = adapter?.getItem(position)

        override fun getItemId(position: Int): Long =
            adapter?.getItemId(position) ?: INVALID_POSITION.toLong()


        private fun setupPromptView(prompt: CharSequence?) {
            if (prompt == null) {
                internalPromptView = null
            } else {
                if (internalPromptView == null) {
                    internalPromptView = TextView(context).apply {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title)
                        } else {
                            setTextAppearance(
                                context,
                                androidx.appcompat.R.style.TextAppearance_AppCompat_Title
                            )
                        }
                        setPaddingRelative(
                            (24 * resources.displayMetrics.density).roundToInt(),
                            (8 * resources.displayMetrics.density).roundToInt(),
                            (24 * resources.displayMetrics.density).roundToInt(),
                            (8 * resources.displayMetrics.density).roundToInt()
                        )
                    }
                    internalPromptView!!.text = prompt
                }
            }
        }
    }

    private inner class BottomSheetPopup(
        val context: Context,
        private var prompt: CharSequence? = null
    ) : SpinnerPopup {

        private var popup: BottomSheetDialog? = null
        private var adapter: ListAdapter? = null
        private var listener: SpinnerPopup.OnDismissListener? = null

        override fun setAdapter(adapter: ListAdapter?) {
            this.adapter = adapter
        }

        override fun setPromptText(hintText: CharSequence?) {
            prompt = hintText
        }

        override fun getPrompt(): CharSequence? = prompt

        override fun show(position: Int) {
            if (adapter == null) {
                return
            }

            popup = BottomSheetDialog(context)
                .apply {
                    val listView = ListView(context).apply {
                        adapter = this@BottomSheetPopup.adapter

                        onItemClickListener =
                            AdapterView.OnItemClickListener { _, v, position, id ->
                                this@MaterialSpinner.selection = position
                                if (this@MaterialSpinner.onItemClickListener != null) {//Fixme Alfred.abdo: should perform click regardless?
                                    this@MaterialSpinner.performItemClick(v, position, id)
                                }
                                dismiss()
                            }
                    }

                    setContentView(
                        if (prompt != null) {
                            val titleView = TextView(context).apply {
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                    setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title)
                                } else {
                                    setTextAppearance(
                                        context,
                                        androidx.appcompat.R.style.TextAppearance_AppCompat_Title
                                    )
                                }
                                setPaddingRelative(
                                    (24 * resources.displayMetrics.density).roundToInt(),
                                    (8 * resources.displayMetrics.density).roundToInt(),
                                    (24 * resources.displayMetrics.density).roundToInt(),
                                    (8 * resources.displayMetrics.density).roundToInt()
                                )
                                text = prompt
                            }

                            LinearLayout(context).apply {
                                orientation = LinearLayout.VERTICAL
                                addView(titleView)
                                addView(listView)
                            }
                        } else {
                            listView
                        }
                    )

                    textDirection = this@MaterialSpinner.textDirection
                    textAlignment = this@MaterialSpinner.textAlignment

                    setOnDismissListener { listener?.onDismiss() }
                }.also { it.show() }
        }

        override fun dismiss() {
            popup?.dismiss()
        }

        override fun setOnDismissListener(listener: SpinnerPopup.OnDismissListener?) {
            this.listener = listener
        }

        override fun getItem(position: Int): Any? = adapter?.getItem(position)

        override fun getItemId(position: Int): Long =
            adapter?.getItemId(position) ?: INVALID_POSITION.toLong()

        override fun isShowing() = popup?.isShowing == true
    }

    /**
     * Creates a new ListAdapter wrapper for the specified adapter.
     *
     * @param [adapter] The SpinnerAdapter to transform into a ListAdapter.
     * @param [dropDownTheme] The theme against which to inflate drop-down views, may be {@null} to use default theme.
     */
    private inner class DropDownAdapter(
        private val adapter: SpinnerAdapter?,
        dropDownTheme: Resources.Theme?
    ) : ListAdapter, SpinnerAdapter {

        private val listAdapter: ListAdapter? =
            when (adapter) {
                is ListAdapter -> adapter
                else -> null
            }


        init {
            dropDownTheme?.let {
                if (adapter is ThemedSpinnerAdapterCompat) {
                    if (adapter.dropDownViewTheme != it) {
                        adapter.dropDownViewTheme = it
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && adapter is ThemedSpinnerAdapter) {
                    if (adapter.dropDownViewTheme == null) {
                        adapter.dropDownViewTheme = it
                    }
                }
            }
        }


        override fun getCount(): Int = adapter?.count ?: 0

        override fun getItem(position: Int): Any? =
            adapter?.let {
                if (position > INVALID_POSITION && position < it.count) it.getItem(position) else null
            }

        override fun getItemId(position: Int): Long =
            adapter?.getItemId(position) ?: INVALID_POSITION.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? =
            getDropDownView(position, convertView, parent)

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? =
            adapter?.getDropDownView(position, convertView, parent)

        override fun hasStableIds(): Boolean = adapter?.hasStableIds() ?: false

        override fun registerDataSetObserver(observer: DataSetObserver) {
            adapter?.registerDataSetObserver(observer)
        }

        override fun unregisterDataSetObserver(observer: DataSetObserver) {
            adapter?.unregisterDataSetObserver(observer)
        }

        /**
         * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this call. Otherwise, return true.
         */
        override fun areAllItemsEnabled(): Boolean = listAdapter?.areAllItemsEnabled() ?: true

        /**
         * If the wrapped SpinnerAdapter is also a ListAdapter, delegate this call. Otherwise, return true.
         */
        override fun isEnabled(position: Int): Boolean = listAdapter?.isEnabled(position) ?: true

        override fun getItemViewType(position: Int): Int = 0

        override fun getViewTypeCount(): Int = 1

        override fun isEmpty(): Boolean = count == 0
    }


    internal class SavedState : AbsSavedState {
        var selection: Int = INVALID_POSITION
        var isShowingPopup: Boolean = false


        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            selection = source.readInt()
            isShowingPopup = source.readByte().toInt() != 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            with(dest) {
                writeInt(selection)
                writeByte((if (isShowingPopup) 1 else 0).toByte())
            }
        }

        override fun toString(): String =
            "MaterialSpinner.SavedState{${Integer.toHexString(System.identityHashCode(this))}" +
                    " selection=$selection, isShowingPopup=$isShowingPopup}"

        companion object CREATOR : Parcelable.ClassLoaderCreator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel, null)
            override fun createFromParcel(parcel: Parcel, loader: ClassLoader): SavedState =
                SavedState(parcel, loader)

            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}