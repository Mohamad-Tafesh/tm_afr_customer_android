package com.africell.africell.features.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.databinding.ItemHelpBinding


class HelpAdapter(
    private val helpList: List<Help>
) : RecyclerView.Adapter<HelpAdapter.ItemVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view = ItemHelpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(view)
    }


    override fun getItemCount(): Int = helpList.size

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = helpList[position]

        holder.viewBinding.run {

            message.setText(item.message)

        }

    }

    class ItemVH(val viewBinding: ItemHelpBinding) : RecyclerView.ViewHolder(viewBinding.root)


}