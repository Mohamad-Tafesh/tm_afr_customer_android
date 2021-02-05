package com.tedmob.africell.features.accountInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.api.dto.AccountBalanceDTO
import com.tedmob.africell.data.entity.MyAccountBalance
import kotlinx.android.synthetic.main.row_free_balance.view.*

class FreeBalanceAdapter(
    private var items: List<AccountBalanceDTO.FreeBalanceX>
) : RecyclerView.Adapter<FreeBalanceAdapter.HomeItemHolder>() {
    interface Callback {
        fun onItemClickListener(item: MyAccountBalance)
    }

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_free_balance, parent, false)
        return HomeItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.itemView.run {
            imageView.setImageURI(item.image)
            title.text = item.title
            description.text = item.description
            priceTxt.text = item.balance + " " + item.balanceUnit
            setOnClickListener {
                item?.let {
                }

            }


        }
    }


    fun setItems(newItems: List<AccountBalanceDTO.FreeBalanceX> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}