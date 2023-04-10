package com.africell.africell.features.afrimoneyBundles

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.databinding.RowBundleBinding

class AfrimoneyBundleAdapter(
    private var items: List<BundleInfo>,
    val callback: Callback
) : RecyclerView.Adapter<AfrimoneyBundleAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: BundleInfo)
    }

    class HomeItemHolder(val viewBinding: RowBundleBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = RowBundleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            try {
                val secondaryColor = Color.parseColor(item.secondaryColor)
                cardViewLayout.strokeColor = secondaryColor
                volumeTxt.setTextColor(secondaryColor)
                subtitleTxt.setTextColor(secondaryColor)
                priceTxt.setTextColor(secondaryColor)
                isActivatedTxt.setTextColor(secondaryColor)
                activateBtn.setBackgroundColor(secondaryColor)
            } catch (e: Exception) {

            }
            volumeTxt.text = item.getFormatVolume()
            subtitleTxt.text = item.subTitles
            priceTxt.text = item.price
            isActivatedTxt.isVisible = item.activate == true
            activateBtn.setOnClickListener {
                callback.onItemClickListener(item)
            }
            root.setOnClickListener {
                callback.onItemClickListener(item)
            }
        }
    }


    fun setItems(newItems: List<BundleInfo> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}