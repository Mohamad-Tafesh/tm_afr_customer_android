package com.tedmob.africell.features.bookNumber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R

import kotlinx.android.synthetic.main.row_book_number.view.*

class BookNumberAdapter(
    private var items: List<String>,
    val callback: Callback
) : RecyclerView.Adapter<BookNumberAdapter.HomeItemHolder>() {

    interface Callback {
        fun onBookNumberClickListener(item: String)
    }

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_book_number, parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.itemView.run {
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