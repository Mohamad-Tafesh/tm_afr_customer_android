package com.africell.africell.ui.spinner.searchable

import android.content.Context
import android.content.DialogInterface
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ListAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.africell.africell.R
import java.io.Serializable

class MaterialSearchableListDialogHolder(private val context: Context) {

    interface OnSearchTextChanged {
        fun afterSearchFilter(strText: String?)
    }

    interface SearchItemClickListener : Serializable {
        fun onSearchableItemClicked(item: MaterialSearchableSpinnerItem, position: Int)
    }


    companion object {
        const val DEFAULT_CLOSE_STR = "CLOSE"
        const val DEFAULT_TITLE_STR = "Select Item"

        private const val SEARCH_ITEM_CLICK_LISTENER = "key_search_item_click_listener"
    }

    private var items: MutableList<out MaterialSearchableSpinnerItem> = mutableListOf()
    private lateinit var adapter: MaterialSearchableAdapter<out MaterialSearchableSpinnerItem>
    private var contentView: View? = null
    private var recyclerView: RecyclerView? = null

    private var title: CharSequence? = null
    private var searchItemClickListener: SearchItemClickListener? = null
    private var onSearchTextChanged: OnSearchTextChanged? = null
    private var searchTextLayout: TextInputLayout? = null
    private var positiveButtonText: String? = null
    private var onClickListener: DialogInterface.OnClickListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var canRetainDialog: Boolean = false
    private var dialog: AlertDialog? = null

    fun create() {
        contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_material_searchable_list, FrameLayout(context), false)

        //fixme this must be fixed
        /*(savedInstanceState?.getSerializable(SEARCH_ITEM_CLICK_LISTENER) as? SearchItemClickListener?)?.let {
            searchItemClickListener = it
        }*/

        contentView?.setupSearchView()

        val mgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(searchTextLayout?.windowToken, 0)

        contentView?.setupRecyclerView()
    }

    /*fun onStart() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        super.onStart()
    }*/

    fun setTitle(title: CharSequence?) {
        this.title = title ?: DEFAULT_TITLE_STR
    }

    fun <T> setItems(items: List<T>) {
        this.items = items.map {
            when (it) {
                is MaterialSearchableSpinnerItem -> it
                is Parcelable -> MaterialSearchableSpinnerParcelableItemDelegate(it)
                else -> MaterialSearchableSpinnerItemDelegate(it)
            }
        }.toMutableList()
    }

    fun setFromAdapter(adapter: ListAdapter) {
        val count = adapter.count
        val items = (0 until count).map { adapter.getItem(it) }
        setItems(items)
    }

    fun setPositiveButton(positiveButtonText: String?) = apply { this.positiveButtonText = positiveButtonText }

    fun setPositiveButton(positiveButtonText: String?, onClickListener: DialogInterface.OnClickListener?) = apply {
        this.positiveButtonText = positiveButtonText
        this.onClickListener = onClickListener
    }


    fun show(): AlertDialog {
        if (contentView == null) {
            create()
        }

        updateRecyclerView()

        dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setView(contentView)
            .apply { if (positiveButtonText != null) this.setPositiveButton(positiveButtonText, onClickListener) }
            .setOnDismissListener(onDismissListener)
            .show()

        return dialog!!
    }


    private fun View.setupSearchView() {
        searchTextLayout = findViewById(R.id.searchTextLayout)
        searchTextLayout?.editText?.run {
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearFocus()
                    true
                } else {
                    false
                }
            }

            addTextChangedListener {
                val s = it?.toString()
                (recyclerView?.adapter as? MaterialSearchableAdapter<out MaterialSearchableSpinnerItem>)?.filter(if (s.isNullOrEmpty()) null else s)
                onSearchTextChanged?.afterSearchFilter(s)
            }

            //clearFocus()
        }
    }

    private fun View.setupRecyclerView() {
        canRetainDialog = items.firstOrNull() is Parcelable

        recyclerView = findViewById(R.id.itemsRV)

        adapter = MaterialSearchableAdapter(
            items,
            onItemClick = { item, position ->
                searchItemClickListener?.onSearchableItemClicked(item, position)
                dialog?.dismiss()
            }
        )
        recyclerView?.run {
            isNestedScrollingEnabled = false
            itemAnimator = null
            setItemViewCacheSize(6)//default 2 is too small for short layout used
            this.adapter = this@MaterialSearchableListDialogHolder.adapter
        }
    }

    private fun updateRecyclerView() {
        canRetainDialog = items.firstOrNull() is Parcelable

        adapter = MaterialSearchableAdapter(
            items,
            onItemClick = { item, position ->
                searchItemClickListener?.onSearchableItemClicked(item, position)
                dialog?.dismiss()
            }
        )
        recyclerView?.adapter = this@MaterialSearchableListDialogHolder.adapter
    }

    /*override fun onSaveInstanceState(): Bundle {
        val outState = super.onSaveInstanceState()

        if (canRetainDialog) {
            outState.putSerializable(SEARCH_ITEM_CLICK_LISTENER, searchItemClickListener)
            //default behavior for [arguments] will be applied
        } else {
            //fixme Cannot serialize if the class that uses it is not fully serializable, because of possible NotSerializableException. Solution: close the dialog for now
            items.clear()
            dismiss()
            //---
        }

        return outState
    }*/


    class Builder(private val context: Context) {

        private var title: CharSequence = DEFAULT_TITLE_STR
        private var searchItemClickListener: SearchItemClickListener? = null
        private var onSearchTextChanged: OnSearchTextChanged? = null
        private var positiveButtonText: String? = null
        private var onClickListener: DialogInterface.OnClickListener? = null
        private var onDismissListener: DialogInterface.OnDismissListener? = null

        fun setTitle(title: CharSequence) = apply { this.title = title }

        fun setPositiveButton(positiveButtonText: String?) = apply { this.positiveButtonText = positiveButtonText }

        fun setPositiveButton(positiveButtonText: String?, onClickListener: DialogInterface.OnClickListener?) = apply {
            this.positiveButtonText = positiveButtonText
            this.onClickListener = onClickListener
        }

        fun setOnSearchableItemClickListener(searchItemClickListener: SearchItemClickListener) = apply {
            this.searchItemClickListener = searchItemClickListener
        }

        fun setOnSearchTextChangedListener(onSearchTextChanged: OnSearchTextChanged) = apply {
            this.onSearchTextChanged = onSearchTextChanged
        }

        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) = apply {
            this.onDismissListener = onDismissListener
        }

        fun create(): MaterialSearchableListDialogHolder {
            val dialog = MaterialSearchableListDialogHolder(context).also {
                it.setTitle(title)
                it.setPositiveButton(positiveButtonText, onClickListener)
                it.searchItemClickListener = searchItemClickListener
                it.onSearchTextChanged = onSearchTextChanged
                it.onDismissListener = onDismissListener

                it.create()
            }
            return dialog
        }

        fun show(): AlertDialog = create().show()
    }
}