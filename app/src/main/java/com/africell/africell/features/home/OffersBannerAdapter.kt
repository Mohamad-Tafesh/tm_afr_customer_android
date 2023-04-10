package com.africell.africell.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.databinding.RowOfferBannerBinding

class OffersBannerAdapter(
    private var items: MutableList<String> = mutableListOf(),
    private val callback: Callback? = null
) : RecyclerView.Adapter<OffersBannerAdapter.ItemVH>() {

    interface Callback {
        fun onItemClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view = RowOfferBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(view)
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        with(holder.viewBinding) {
            image.setImageURI(item)
        }

        holder.itemView.setOnClickListener {
            callback?.onItemClick(item)
        }

    }

    class ItemVH(val viewBinding: RowOfferBannerBinding) : RecyclerView.ViewHolder(viewBinding.root)

    fun setItems(newItems: List<String> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }


}