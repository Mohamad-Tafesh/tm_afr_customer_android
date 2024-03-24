package com.africell.africell.features.bundles

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.africell.africell.BuildConfig
import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.databinding.RowBundleBinding
import com.africell.africell.databinding.RowBundleGambiaBinding
import com.africell.africell.ui.animation.onClick

class BundleAdapter(
    private var items: List<BundleInfo>,
    val callback: Callback
) : RecyclerView.Adapter<ViewHolder>() {

    interface Callback {
        fun onItemClickListener(item: BundleInfo)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (BuildConfig.FLAVOR == "gambia") {
            HomeItemGambiaHolder(RowBundleGambiaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            HomeItemHolder(RowBundleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    inner class HomeItemHolder(val viewBinding: RowBundleBinding) : ViewHolder(viewBinding.root) {

        fun onBind(item: BundleInfo) {
            viewBinding.run {
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

                activateBtn.onClick {
                    callback.onItemClickListener(item)
                }
                root.onClick {
                    callback.onItemClickListener(item)
                }
            }
        }
    }

    inner class HomeItemGambiaHolder(val viewBinding: RowBundleGambiaBinding) : ViewHolder(viewBinding.root) {
        fun onBind(item: BundleInfo) {
            viewBinding.run {
                if (item.extraInfo == null) {
                    dividerMd.isVisible = false
                    leftRightLl.isVisible = false
                }


                cardViewLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.primaryColor.toString()))
                cardViewLayout.strokeWidth = Color.parseColor(item.primaryColor.toString())

                activateBtn.backgroundTintList = ColorStateList.valueOf(Color.parseColor(item.secondaryColor.toString()))

                dataLiftTitleTv.text = item.extraInfo?.main?.title.orEmpty()
                dataLimitValueTv.text = item.extraInfo?.main?.value.orEmpty()
                leftTitleTv.text = item.extraInfo?.left?.title.orEmpty()
                leftValueTv.text = item.extraInfo?.left?.value.orEmpty()
                rightTitleTv.text = item.extraInfo?.right?.title.orEmpty()
                rightValueTv.text = item.extraInfo?.right?.value.orEmpty()
                bundleSizeTv.text = item.commercialName.orEmpty()
                priceTv.text = item.price

                activateBtn.onClick {
                    callback.onItemClickListener(item)
                }
                root.onClick {
                    callback.onItemClickListener(item)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is HomeItemHolder -> holder.onBind(item)
            is HomeItemGambiaHolder -> holder.onBind(item)
        }
    }


    fun setItems(newItems: List<BundleInfo> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}