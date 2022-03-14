package com.africell.africell.features.afrimoney

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.entity.MoneyTransferOptions
import kotlinx.android.synthetic.main.row_afrimoney.view.*


class AfrimoneyOptionsAdapter(
    private var items: MutableList<MoneyTransferOptions> = mutableListOf(),
    private val onCLick: ((MoneyTransferOptions) -> Unit)
) : RecyclerView.Adapter<AfrimoneyOptionsAdapter.ItemVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_afrimoney, parent, false)
        return ItemVH(itemView)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        holder.itemView.run {
            title.text = item.name.orEmpty()
            image.setImageResource(item.icon)
            lineHorizontal.visibility = if (position < 2) View.INVISIBLE else View.VISIBLE
            lineVertical.visibility = if (position % 2 == 0) View.INVISIBLE else View.VISIBLE
        }
        val itemView = holder.itemView
        itemView.setOnClickListener { onCLick.invoke(item) }
    }

    override fun getItemCount(): Int = items.size
    class ItemVH(view: View) : RecyclerView.ViewHolder(view)


}