package com.tedmob.africell.ui.spinner.searchable

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.ui.inflate

class MaterialSearchableAdapter<T : MaterialSearchableSpinnerItem>(
    items: List<T>,
    private val onItemClick: (item: T, position: Int) -> Unit
) :
    RecyclerView.Adapter<MaterialSearchableAdapter.Item>() {

    class Item(view: View) : RecyclerView.ViewHolder(view) {
        val text by lazy { view.findViewById<TextView>(android.R.id.text1) }
    }


    private val originalValues: List<T> = items
    private var items: List<T> = originalValues.toMutableList()


    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        return Item(parent.inflate(android.R.layout.simple_list_item_1))
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item = items[position]

        holder.run {
            text.text = item.toDisplayString()

            itemView.setOnClickListener {
                val actualItemPosition = originalValues.indexOf(item)
                onItemClick(item, actualItemPosition)
            }
        }
    }


    fun filter(query: String?) {
        if (query == null) {
            items = originalValues.toMutableList()
        } else {
            items = originalValues.filter { it.matchesQuery(query) }
        }
        notifyDataSetChanged()
    }
}