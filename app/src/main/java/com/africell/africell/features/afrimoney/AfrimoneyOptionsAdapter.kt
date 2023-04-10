package com.africell.africell.features.afrimoney

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.entity.MoneyTransferOptions
import com.africell.africell.databinding.RowAfrimoneyBinding


class AfrimoneyOptionsAdapter(
    private var items: MutableList<MoneyTransferOptions> = mutableListOf(),
    private val onCLick: ((MoneyTransferOptions) -> Unit)
) : RecyclerView.Adapter<AfrimoneyOptionsAdapter.ItemVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val itemViewBinding = RowAfrimoneyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(itemViewBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            title.text = item.name.orEmpty()
            image.setImageResource(item.icon)
            lineHorizontal.visibility = if (position < 2) View.INVISIBLE else View.VISIBLE
            lineVertical.visibility = if (position % 2 == 0) View.INVISIBLE else View.VISIBLE
        }
        val itemView = holder.itemView
        itemView.setOnClickListener { onCLick.invoke(item) }
    }

    override fun getItemCount(): Int = items.size
    class ItemVH(val viewBinding: RowAfrimoneyBinding) : RecyclerView.ViewHolder(viewBinding.root)


}