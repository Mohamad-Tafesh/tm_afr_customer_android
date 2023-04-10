package com.africell.africell.features.accountInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.entity.MyAccountBalanceCategories
import com.africell.africell.databinding.RowAccountBalanceCategoriesBinding


class AccountBalanceCategoriesAdapter(
    private var items: MutableList<MyAccountBalanceCategories> = mutableListOf(),
) : RecyclerView.Adapter<AccountBalanceCategoriesAdapter.ItemVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            RowAccountBalanceCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            title.text = item.title
            recyclerView.adapter = AccountBalanceAdapter(item.myAccountBalance)
        }

    }

    override fun getItemCount(): Int = items.size
    class ItemVH(val viewBinding: RowAccountBalanceCategoriesBinding) : RecyclerView.ViewHolder(viewBinding.root)

    fun setItems(newItems: List<MyAccountBalanceCategories>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}