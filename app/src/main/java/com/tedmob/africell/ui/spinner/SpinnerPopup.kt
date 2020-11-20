package com.tedmob.africell.ui.spinner

import android.widget.ListAdapter

/**
 * Implements some sort of popup selection interface for selecting a spinner option.
 * Allows for different spinner modes.
 */
internal interface SpinnerPopup {

    /**
     * Listener that is called when this popup window is dismissed.
     */
    interface OnDismissListener {
        /**
         * Called when this popup window is dismissed.
         */
        fun onDismiss()
    }

    /**
     * Set hint text to be displayed to the user. This should provide a description of the choice being made.
     *
     * @param [hintText] Hint text to set.
     */
    fun setPromptText(hintText: CharSequence?)

    /**
     * @return The prompt to display when the dialog is shown
     */
    fun getPrompt(): CharSequence?

    /**
     * Sets the adapter that provides the data and the views to represent the data in this popup window.
     *
     * @param [adapter] The adapter to use to create this window's content.
     */
    fun setAdapter(adapter: ListAdapter?)

    /**
     * Show the popup
     */
    fun show(position: Int)

    /**
     * Dismiss the popup
     */
    fun dismiss()

    /**
     * Set a listener to receive a callback when the popup is dismissed.
     *
     * @param [listener] Listener that will be notified when the popup is dismissed.
     */
    fun setOnDismissListener(listener: OnDismissListener?)

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param [position] Position of the item whose data we want within the adapter's data set.
     * @return The data at the specified position.
     */
    fun getItem(position: Int): Any?

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param [position] The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    fun getItemId(position: Int): Long

    /**
     * @return true if the popup is showing, false otherwise.
     */
    fun isShowing(): Boolean
}