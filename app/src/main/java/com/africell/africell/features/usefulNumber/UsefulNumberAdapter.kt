package com.africell.africell.features.usefulNumber

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.api.dto.UsefulNumberDTO
import com.africell.africell.databinding.RowUsefulNumberBinding

class UsefulNumberAdapter(
    private var items: List<UsefulNumberDTO>,
    val callback: Callback
) : RecyclerView.Adapter<UsefulNumberAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: UsefulNumberDTO)
    }

    class HomeItemHolder(val viewBinding: RowUsefulNumberBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = RowUsefulNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]

        holder.viewBinding.run {
            title.text = item?.name
            description.text = item?.description
            number.text = item?.number.orEmpty()
            root.setOnClickListener {
                item?.let {
                    callback.onItemClickListener(it)
                }

            }

        }
    }


    fun setItems(newItems: List<UsefulNumberDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}