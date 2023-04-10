package com.africell.africell.features.lineRecharge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.api.dto.RechargeCardDTO
import com.africell.africell.databinding.RowLineRechargeBinding

class LineRechargeAdapter(
    private var items: List<RechargeCardDTO>,
    val callback: Callback
) : RecyclerView.Adapter<LineRechargeAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: RechargeCardDTO)
    }

    class HomeItemHolder(val viewBinding: RowLineRechargeBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = RowLineRechargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            title.text = item?.rechargeCardName
            description.text = item?.rechargeCardDescription
            priceTxt.text = item?.rechargeCardPrice
            image.setImageURI(item.rechargeCardImageName)
            root.setOnClickListener {
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