package com.tedmob.africell.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.entity.MyAccountBalance
import kotlinx.android.synthetic.main.row_balance.view.*

class HomeBalanceAdapter(
    private var items: List<MyAccountBalance>
) : RecyclerView.Adapter<HomeBalanceAdapter.HomeItemHolder>() {
    interface Callback {
        fun onItemClickListener(item: MyAccountBalance)
    }

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_balance, parent, false)
        val displayMetrics = parent.context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val layoutParams = itemView.layoutParams
        val padding = parent.context.resources.getDimensionPixelSize(R.dimen.spacing_small)
        layoutParams.width = width / 3
        layoutParams.height = width / 3
        itemView.layoutParams = layoutParams

        return HomeItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.itemView.run {
            progressBar.max = item.maxValue?.toDoubleOrNull()?.toInt() ?:100
            progressBar.progress = item?.currentBalance?.toDoubleOrNull()?.toInt() ?:0

            nameTxt.text=item?.title
            valueTxt.text=item?.currentBalance +" "+item?.unit
            expiryDateTxt.text="Expiry Date: "+item?.expiryDate

            setOnClickListener {
                item?.let {
                }

            }


        }
    }


    fun setItems(newItems: List<MyAccountBalance> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}