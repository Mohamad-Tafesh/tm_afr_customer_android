package com.tedmob.africell.features.settings.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R

class SettingsMenuListAdapter :
    RecyclerView.Adapter<SettingsMenuListAdapter.SettingsMenuListItemHolder>() {

    var itemSelectedIndex: Int? = null
    var onItemClickListener: ((i: Int) -> Unit)? = null
    var entries: List<CharSequence> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var popupMode: Int =
        SettingsMenuPopupWindow.POPUP_MENU
    var horizontalPadding: Int = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SettingsMenuListItemHolder =
        SettingsMenuListItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.settings_menu_item, parent, false),
            onItemClickListener
        )

    override fun onBindViewHolder(holder: SettingsMenuListItemHolder, position: Int) {
        holder.bind(entries, position, itemSelectedIndex, popupMode, horizontalPadding)
    }

    override fun getItemCount(): Int = entries.size


    class SettingsMenuListItemHolder(view: View, onItemClickListener: ((i: Int) -> Unit)?) :
        RecyclerView.ViewHolder(view) {

        private val checkedTextView: CheckedTextView = itemView.findViewById(android.R.id.text1)


        init {
            itemView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }

        fun bind(
            entries: List<CharSequence>,
            position: Int,
            selectedIndex: Int?,
            popupMode: Int,
            horizontalPadding: Int
        ) {
            checkedTextView.run {
                text = entries[position]
                isChecked = position == selectedIndex
                maxLines = if (popupMode == SettingsMenuPopupWindow.DIALOG) Int.MAX_VALUE else 1

                val paddingVertical: Int = paddingTop
                setPadding(horizontalPadding, paddingVertical, horizontalPadding, paddingVertical)
            }
        }
    }
}