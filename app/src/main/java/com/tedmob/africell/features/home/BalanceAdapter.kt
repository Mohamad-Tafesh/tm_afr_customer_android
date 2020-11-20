package com.tedmob.africell.features.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.api.dto.BalanceDTO
import kotlinx.android.synthetic.main.row_balance.view.*

class BalanceAdapter(
    private var items: List<BalanceDTO>
) : RecyclerView.Adapter<BalanceAdapter.HomeItemHolder>() {
    interface Callback {
        fun onItemClickListener(item: BalanceDTO)
    }
    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_balance, parent, false)
        val displayMetrics = parent.context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val layoutParams = itemView.layoutParams
        val padding = parent.context.resources.getDimensionPixelSize(R.dimen.spacing_small)
        layoutParams.width = width/3
        layoutParams.height= width/3
        itemView.layoutParams = layoutParams

        return HomeItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]



        holder.itemView.run {
            progressBar.setProgress(50,true)
          val ly=  progressBar.layoutParams
            ly.height= ly.width
            progressBar.layoutParams=ly
            progressBar.max=100
            progressBar.isIndeterminate=false
        //                title.text=item?.name
                setOnClickListener {
                    item?.let {
                    }

                }

        }
    }


    fun setItems(newItems: List<BalanceDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}