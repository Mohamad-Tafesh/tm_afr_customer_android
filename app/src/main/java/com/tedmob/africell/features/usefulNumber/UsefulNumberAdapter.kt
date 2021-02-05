package com.tedmob.africell.features.usefulNumber

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.data.api.dto.UsefulNumberDTO
import kotlinx.android.synthetic.main.row_useful_number.view.*

class UsefulNumberAdapter(
    private var items: List<UsefulNumberDTO>,
    val callback: Callback
) : RecyclerView.Adapter<UsefulNumberAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: UsefulNumberDTO)
    }

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_useful_number, parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]

        holder.itemView.run {
            title.text = item?.name
            description.text = item?.description
            number.text=item?.number.orEmpty()
            setOnClickListener {
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