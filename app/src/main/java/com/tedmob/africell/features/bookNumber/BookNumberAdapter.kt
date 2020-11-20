package com.tedmob.africell.features.bookNumber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.api.dto.BookNumberDTO
import kotlinx.android.synthetic.main.row_book_number.view.*

class BookNumberAdapter(
    private var items: List<BookNumberDTO>,
    val callback: Callback
) : RecyclerView.Adapter<BookNumberAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: BookNumberDTO)
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
            title.text = item?.title
            setOnClickListener {
                item?.let {
                    callback.onItemClickListener(it)
                }
            }
        }
    }


    fun setItems(newItems: List<BookNumberDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}