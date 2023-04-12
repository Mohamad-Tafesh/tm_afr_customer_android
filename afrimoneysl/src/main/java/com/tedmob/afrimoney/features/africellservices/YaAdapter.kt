package com.tedmob.afrimoney.features.africellservices

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.afrimoney.databinding.ItemYaSpecialBinding
import okhttp3.internal.notifyAll


class YaAdapter(
    private var items: List<String>,
    private val context: Context,

    ) : RecyclerView.Adapter<YaAdapter.ItemVH>() {

    var selectedindex: Int = -1

    interface Callback {
        fun onItemClickListener(item: String)
    }



    class ItemVH(val view: ItemYaSpecialBinding) : RecyclerView.ViewHolder(view.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: String, context: Context, position: Int, selectedindex: Int) {
            view.run {
                name.text = item
                rb.isChecked = selectedindex == position

            }
        }

    }


    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val itemView =
            ItemYaSpecialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(itemView)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        holder.bind(item, context, position, selectedindex)
        holder.view.root.setOnClickListener {
            val oldPosition = selectedindex
            selectedindex = position
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedindex)
        }

        holder.view.rb.setOnClickListener {
            val oldPosition = selectedindex
            selectedindex = position
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedindex)
        }

    }


    fun setItems(newItems: List<String> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}