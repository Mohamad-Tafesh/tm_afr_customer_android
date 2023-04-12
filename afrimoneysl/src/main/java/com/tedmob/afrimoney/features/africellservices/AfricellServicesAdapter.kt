package com.tedmob.afrimoney.features.africellservices

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tedmob.afrimoney.data.api.dto.AfricellServicesDTO
import com.tedmob.afrimoney.data.entity.BundlelistParent
import com.tedmob.afrimoney.databinding.ItemAfricellServiceBinding
import com.tedmob.afrimoney.databinding.ItemAfricellServicesBinding
import com.tedmob.afrimoney.databinding.ItemYaSpecialBinding
import okhttp3.internal.notifyAll


class AfricellServicesAdapter(
    private var items: List<BundlelistParent>,
    private val context: Context,

    ) : RecyclerView.Adapter<AfricellServicesAdapter.ItemVH>() {

    var selectedindex: Int = -1

    interface Callback {
        fun onItemClickListener(item: BundlelistParent)
    }


    class ItemVH(val view: ItemAfricellServicesBinding) : RecyclerView.ViewHolder(view.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: BundlelistParent, context: Context, position: Int, selectedindex: Int) {
            view.run {

                img.load(item.icon)
                title.setText(item.displayName)

            }
        }

    }


    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val itemView =
            ItemAfricellServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(itemView)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        holder.bind(item, context, position, selectedindex)

    }


    fun setItems(newItems: List<BundlelistParent> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}