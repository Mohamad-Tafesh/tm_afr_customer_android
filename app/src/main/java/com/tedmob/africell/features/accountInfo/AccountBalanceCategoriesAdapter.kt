package com.tedmob.africell.features.accountInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.entity.MyAccountBalanceCategories
import kotlinx.android.synthetic.main.row_account_balance_categories.view.*


class AccountBalanceCategoriesAdapter(
    private var items: MutableList<MyAccountBalanceCategories> = mutableListOf(),
) : RecyclerView.Adapter<AccountBalanceCategoriesAdapter.ItemVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            LayoutInflater.from(parent.context).inflate(R.layout.row_account_balance_categories, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        with(holder.itemView) {
            title.text = item.title
            recyclerView.adapter = AccountBalanceAdapter(item.myAccountBalance)
        }

    }

    override fun getItemCount(): Int = items.size
    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setItems(newItems: List<MyAccountBalanceCategories>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}