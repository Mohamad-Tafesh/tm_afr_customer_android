package com.africell.africell.features.accountsNumber

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.entity.SubAccount
import com.africell.africell.databinding.RowAccountsBinding


class AccountAdapter(
    private var items: MutableList<SubAccount> = mutableListOf(),
    private val mainAccount: String?,
    private val showDeleteIcon: Boolean,
    private val callback: CallBack
) : RecyclerView.Adapter<AccountAdapter.ItemVH>() {

    interface CallBack {
        fun onItemClick(item: SubAccount)
        fun deleteAccount(item: SubAccount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(RowAccountsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        with(holder.viewBinding) {
            title.text = item.account
            deleteIcon.isVisible = showDeleteIcon && item.account != mainAccount
            deleteIcon.setOnClickListener {
                callback.deleteAccount(item)
            }
        }

        val itemView = holder.itemView
        itemView.setOnClickListener { callback.onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size
    class ItemVH(val viewBinding: RowAccountsBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    }


    fun setItems(newItems: List<SubAccount>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}