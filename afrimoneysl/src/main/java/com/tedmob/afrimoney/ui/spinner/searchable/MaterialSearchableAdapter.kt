package com.tedmob.afrimoney.ui.spinner.searchable

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.ui.inflate
import com.tedmob.afrimoney.ui.spinner.ImageProvider

class MaterialSearchableAdapter<T : MaterialSearchableSpinnerItem>(
    items: List<T>,
    private val onItemClick: (item: T, position: Int) -> Unit
) : RecyclerView.Adapter<MaterialSearchableAdapter.Item>() {

    class Item(view: View) : RecyclerView.ViewHolder(view) {
        val text by lazy { view.findViewById<TextView>(android.R.id.text1) }
        val image: ImageView? by lazy { view.findViewById<ImageView>(R.id.image) }
    }


    private val originalValues: List<T> = items
    private var items: List<T> = originalValues.toMutableList()
    private val potentiallyHasImage: Boolean by lazy { items.any { it is ImageProvider } }


    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        return Item(parent.inflate(if (potentiallyHasImage) R.layout.list_item_with_image else android.R.layout.simple_list_item_1))
    }

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item = items[position]

        holder.run {
            text.text = item.toDisplayString()

            if (item is ImageProvider) {
                image?.visibility = View.VISIBLE
                image?.setImageDrawable((item as? ImageProvider)?.getImage(itemView.context))
            } else {
                image?.visibility = View.GONE
                image?.setImageDrawable(null)
            }

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