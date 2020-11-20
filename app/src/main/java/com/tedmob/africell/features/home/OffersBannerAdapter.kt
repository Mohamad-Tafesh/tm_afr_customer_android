package com.tedmob.africell.features.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.tedmob.africell.R
import kotlinx.android.synthetic.main.row_offer_banner.view.*

class OffersBannerAdapter(
    private var items: MutableList<String> = mutableListOf(),
    private val callback: Callback? = null
) : RecyclerView.Adapter<OffersBannerAdapter.ItemVH>() {

    interface Callback {
        fun onItemClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_offer_banner, parent, false)
        return ItemVH(view)
    }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        holder.itemView.run {
            image.setImageURI(item)
        }

        holder.itemView.setOnClickListener {
            callback?.onItemClick(item)
        }

    }

    class ItemVH(private val view: View) : RecyclerView.ViewHolder(view)

    fun setItems(newItems: List<String> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }




}