package com.africell.africell.features.accountsNumber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.entity.SubAccount
import kotlinx.android.synthetic.main.row_accounts.view.*


class AccountAdapter(
    private var items: MutableList<SubAccount> = mutableListOf(),
    private val mainAccount:String?,
    private val showDeleteIcon:Boolean,
    private val callback: CallBack
) : RecyclerView.Adapter<AccountAdapter.ItemVH>() {

    interface CallBack {
        fun onItemClick(item: SubAccount)
        fun deleteAccount(item: SubAccount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(LayoutInflater.from(parent.context).inflate(R.layout.row_accounts, parent, false))
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        with(holder.itemView) {
            title.text = item.account
            deleteIcon.isVisible = showDeleteIcon  && item.account!= mainAccount
            deleteIcon.setOnClickListener {
                callback.deleteAccount(item)
            }
        }

        val itemView = holder.itemView
        itemView.setOnClickListener { callback.onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size
    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }


    fun setItems(newItems: List<SubAccount>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}