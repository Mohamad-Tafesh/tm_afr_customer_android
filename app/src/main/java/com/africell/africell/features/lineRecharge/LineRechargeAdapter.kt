package com.africell.africell.features.lineRecharge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.api.dto.RechargeCardDTO
import kotlinx.android.synthetic.main.row_line_recharge.view.*

class LineRechargeAdapter(
    private var items: List<RechargeCardDTO>,
    val callback: Callback
) : RecyclerView.Adapter<LineRechargeAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: RechargeCardDTO)
    }

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_line_recharge, parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.itemView.run {
            title.text = item?.rechargeCardName
            description.text = item?.rechargeCardDescription
            priceTxt.text= item?.rechargeCardPrice
            image.setImageURI(item.rechargeCardImageName)
            setOnClickListener {
                item?.let {
                    callback.onItemClickListener(it)
                }
            }
        }
    }


    fun setItems(newItems: List<RechargeCardDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }


}