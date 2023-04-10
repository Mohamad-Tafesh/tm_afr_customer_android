package com.africell.africell.features.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.data.api.dto.ServicesDTO
import com.africell.africell.databinding.RowServicesBinding

class ServicesAdapter(
    private var items: List<ServicesDTO>,
    val callback: Callback
) : RecyclerView.Adapter<ServicesAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: ServicesDTO)
    }

    class HomeItemHolder(val viewBinding: RowServicesBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = RowServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]

        holder.viewBinding.run {
            //image.setImageURI(item?.image)
            // distance.text=item?.displayDistance()

            title.text = item?.name

            subscribedTxt.visibility = if (item.isActive == true) View.VISIBLE else View.GONE
            subscribeBtn.visibility = if (item.isActive == true) View.GONE else View.VISIBLE
            description.text = item?.subTitle
            root.setOnClickListener {
                callback.onItemClickListener(item)
            }
            subscribeBtn.setOnClickListener {
                callback.onItemClickListener(item)
            }

        }
    }


    fun setItems(newItems: List<ServicesDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}