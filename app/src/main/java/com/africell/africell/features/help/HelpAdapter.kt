package com.africell.africell.features.help

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import kotlinx.android.synthetic.main.item_help.view.*


class HelpAdapter(
    private val helpList:List<Help>
) : RecyclerView.Adapter<HelpAdapter.ItemVH>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_help, parent, false)
        return ItemVH(view)
    }


    override fun getItemCount(): Int = helpList.size

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = helpList[position]

        holder.itemView.run {

            message.setText(item.message)

        }

    }

    class ItemVH(private val view: View) : RecyclerView.ViewHolder(view)



}