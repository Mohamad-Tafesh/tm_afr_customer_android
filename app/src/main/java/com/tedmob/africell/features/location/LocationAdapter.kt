package com.tedmob.africell.features.location

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.api.dto.LocationDTO
import kotlinx.android.synthetic.main.row_location.view.*

class LocationAdapter(
    private var items: List<LocationDTO>,
    val callback: Callback
) : RecyclerView.Adapter<LocationAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: LocationDTO)
    }
    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_location, parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]

        holder.itemView.run {
                //image.setImageURI(item?.image)
                distance.text=item?.displayDistance()
                title.text=item?.title
                description.text=item?.description
                setOnClickListener {
                    item?.let {
                        callback.onItemClickListener(it)
                    }

                }

        }
    }


    fun setItems(newItems: List<LocationDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}