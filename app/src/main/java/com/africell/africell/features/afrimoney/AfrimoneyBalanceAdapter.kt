package com.africell.africell.features.afrimoney

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.api.dto.MoneyTransferBalanceDTO
import com.africell.africell.databinding.RowBalanceBinding

class AfrimoneyBalanceAdapter(
    private var items: List<MoneyTransferBalanceDTO>
) : RecyclerView.Adapter<AfrimoneyBalanceAdapter.HomeItemHolder>() {
    interface Callback {
        fun onItemClickListener(item: MoneyTransferBalanceDTO)
    }

    class HomeItemHolder(val viewBinding: RowBalanceBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val itemViewBinding = RowBalanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val displayMetrics = parent.context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val layoutParams = itemViewBinding.root.layoutParams
        val padding = parent.context.resources.getDimensionPixelSize(R.dimen.spacing_small)
        layoutParams.width = width / 3
        layoutParams.height = width / 3
        itemViewBinding.root.layoutParams = layoutParams

        return HomeItemHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            progressBar.max = item.balanceValue?.toDoubleOrNull()?.toInt() ?: 100
            progressBar.progress = item?.balanceValue?.toDoubleOrNull()?.toInt() ?: 0

            nameTxt.text = item?.walletName
            valueTxt.text = item?.balanceValue + " " + item?.currency
            expiryDateTxt.text = item?.subtitle

            root.setOnClickListener {

            }


        }
    }


    fun setItems(newItems: List<MoneyTransferBalanceDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}