package com.africell.africell.features.afrimoney

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.api.dto.MoneyTransferBalanceDTO

import kotlinx.android.synthetic.main.row_balance.view.*

class AfrimoneyBalanceAdapter(
    private var items: List<MoneyTransferBalanceDTO>
) : RecyclerView.Adapter<AfrimoneyBalanceAdapter.HomeItemHolder>() {
    interface Callback {
        fun onItemClickListener(item: MoneyTransferBalanceDTO)
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
            progressBar.max = item.balanceValue?.toDoubleOrNull()?.toInt() ?:100
            progressBar.progress = item?.balanceValue?.toDoubleOrNull()?.toInt() ?:0

            nameTxt.text=item?.walletName
            valueTxt.text=item?.balanceValue +" "+item?.currency
            expiryDateTxt.text=item?.subtitle

            setOnClickListener {

            }


        }
    }


    fun setItems(newItems: List<MoneyTransferBalanceDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}