package com.tedmob.afrimoney.ui.spinner

import android.view.View

/**
 * Interface for a callback to be invoked when an item in this view has been selected.
 */
interface OnItemSelectedListener {

    /**
     * Callback method to be invoked when an item in this view has been selected.
     * This callback is invoked only when the newly selected position is different from the previously selected position or if
     * there was no selected item.
     * Implementers can call getItemAtPosition(position) if they need to access the data associated with the selected item.
     *
     * @param [parent] The View where the selection happened.
     * @param [view] The view within the Adapter that was clicked.
     * @param [position] The position of the view in the adapter.
     * @param [id] The row id of the item that is selected.
     */
    fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long)

    /**
     * Callback method to be invoked when the selection disappears from this view.
     * The selection can disappear for instance when touch is activated or when the adapter becomes empty.
     *
     * @param [parent] The View that now contains no selected item.
     */
    fun onNothingSelected(parent: MaterialSpinner)
}