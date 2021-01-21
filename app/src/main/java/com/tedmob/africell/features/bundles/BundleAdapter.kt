package com.tedmob.africell.features.bundles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.api.dto.BundleInfo
import kotlinx.android.synthetic.main.row_bundle.view.*

class BundleAdapter(
    private var items: List<BundleInfo>,
    val callback: Callback
) : RecyclerView.Adapter<BundleAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: BundleInfo)
    }

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_bundle, parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.itemView.run {
            volumeTxt.text = item.getFormatVolume()
            validityTxt.text = item.getFormatValidity()
            priceTxt.text ="Price: "+ item.price
            isActivatedTxt.isVisible = item.activate == true
            setOnClickListener {
                callback.onItemClickListener(item)

            }
        }
    }


    fun setItems(newItems: List<BundleInfo> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}