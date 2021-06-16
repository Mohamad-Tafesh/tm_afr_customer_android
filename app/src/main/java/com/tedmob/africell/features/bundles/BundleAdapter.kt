package com.tedmob.africell.features.bundles

import android.graphics.Color
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
    private val primaryColor: String?,
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
            try {
                val primaryCol=Color.parseColor(primaryColor)
                cardViewLayout.strokeColor = primaryCol
                volumeTxt.setTextColor(primaryCol)
                subtitleTxt.setTextColor(primaryCol)
                priceTxt.setTextColor(primaryCol)
                isActivatedTxt.setTextColor(primaryCol)
                activateBtn.setBackgroundColor(primaryCol)
            } catch (e: Exception) {

            }
            volumeTxt.text = item.getFormatVolume()
            subtitleTxt.text = item.subTitles
            priceTxt.text = item.price
            isActivatedTxt.isVisible = item.activate == true
            activateBtn.setOnClickListener {
                callback.onItemClickListener(item)
            }
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