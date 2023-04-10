package com.africell.africell.features.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.api.dto.LocationDTO
import com.africell.africell.databinding.RowLocationBinding

class LocationAdapter(
    private var items: List<LocationDTO>,
    private var latitude: Double?,
    private var longitude: Double?,
    val callback: Callback
) : RecyclerView.Adapter<LocationAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: LocationDTO)
    }

    class HomeItemHolder(val viewBinding: RowLocationBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = RowLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]

        holder.viewBinding.run {
            //image.setImageURI(item?.image)
            distance.text = item?.displayDistance(latitude, longitude)
            title.text = item?.shopName
            description.text = item?.address
            root.setOnClickListener {
                item?.let {
                    callback.onItemClickListener(it)
                }

            }

        }
    }


    fun setItems(newItems: List<LocationDTO> = mutableListOf(), lat: Double?, lng: Double?) {
        items = newItems.toMutableList()
        latitude = lat
        longitude = lng
        notifyDataSetChanged()
    }
}