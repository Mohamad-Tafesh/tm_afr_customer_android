package com.africell.africell.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.entity.MyAccountBalance
import com.africell.africell.databinding.RowBalanceBinding
import com.africell.africell.util.removeTime

class HomeBalanceAdapter(
    private var items: List<MyAccountBalance>
) : RecyclerView.Adapter<HomeBalanceAdapter.HomeItemHolder>() {
    interface Callback {
        fun onItemClickListener(item: MyAccountBalance)
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
        //itemViewBinding.root.layoutParams = layoutParams

        return HomeItemHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            progressBar.max = item.maxValue?.toDoubleOrNull()?.toInt() ?: 100
            progressBar.progress = item?.currentBalance?.toDoubleOrNull()?.toInt() ?: 0

            nameTxt.text = item?.title
            valueTxt.text = item?.currentBalance + " " + item?.unit
            expiryDateTxt.text = root.context.getString(R.string.expiry_date_) + item?.expiryDate?.removeTime()

            root.setOnClickListener {

            }


        }
    }


    fun setItems(newItems: List<MyAccountBalance> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}