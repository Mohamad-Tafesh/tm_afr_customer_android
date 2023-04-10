package com.africell.africell.features.bundles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.BuildConfig.FLAVOR
import com.africell.africell.data.api.dto.BundleCategoriesDTO
import com.africell.africell.databinding.RowBundleCategoryBinding

class BundleCategoriesAdapter(
    private var items: List<BundleCategoriesDTO>,
    val callback: Callback
) : RecyclerView.Adapter<BundleCategoriesAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: BundleCategoriesDTO)
    }

    class HomeItemHolder(val viewBinding: RowBundleCategoryBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = RowBundleCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            image.setImageURI(item?.image)
            title.text = item?.categoryName.orEmpty()
            root.setOnClickListener {
                item?.let {
                    callback.onItemClickListener(it)
                }
            }
            bundleTxt.isVisible = FLAVOR != "drc"
        }
    }


    fun setItems(newItems: List<BundleCategoriesDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}