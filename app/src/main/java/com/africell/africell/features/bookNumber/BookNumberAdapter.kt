package com.africell.africell.features.bookNumber

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.databinding.RowBookNumberBinding

class BookNumberAdapter(
    private var items: List<String>,
    val callback: Callback
) : RecyclerView.Adapter<BookNumberAdapter.HomeItemHolder>() {

    interface Callback {
        fun onBookNumberClickListener(item: String)
    }

    class HomeItemHolder(val viewBinding: RowBookNumberBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = RowBookNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            //image.setImageURI(item?.image)
            title.text = item

            bookBtn.setOnClickListener {
                callback.onBookNumberClickListener(item)
            }
        }
    }


    fun setItems(newItems: List<String> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}