package com.africell.africell.features.accountInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.api.dto.AccountBalanceDTO
import com.africell.africell.data.entity.MyAccountBalance
import com.africell.africell.databinding.RowFreeBalanceBinding

class FreeBalanceAdapter(
    private var items: List<AccountBalanceDTO.FreeBalanceX>
) : RecyclerView.Adapter<FreeBalanceAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: MyAccountBalance)
    }

    class HomeItemHolder(val viewBinding: RowFreeBalanceBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val itemView = RowFreeBalanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            imageView.setImageURI(item.image)
            title.text = item.title
            description.text = item.description
            priceTxt.text = item.balance + " " + item.balanceUnit
            root.setOnClickListener {
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