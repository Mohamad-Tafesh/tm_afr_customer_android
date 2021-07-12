package com.tedmob.africell.features.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.data.api.dto.ServicesDTO
import kotlinx.android.synthetic.main.row_services.view.*

class ServicesAdapter(
    private var items: List<ServicesDTO>,
    val callback: Callback
) : RecyclerView.Adapter<ServicesAdapter.HomeItemHolder>() {

    interface Callback {
        fun onItemClickListener(item: ServicesDTO)
    }

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_services, parent, false)
        return HomeItemHolder(v)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]

        holder.itemView.run {
            //image.setImageURI(item?.image)
            // distance.text=item?.displayDistance()

            title.text = item?.name

            subscribedTxt.visibility = if (item.isActive == true) View.VISIBLE else View.GONE
            subscribeBtn.visibility = if (item.isActive == true) View.GONE else View.VISIBLE
            description.text = item?.subTitle
            setOnClickListener {
                callback.onItemClickListener(item)
            }
            subscribedTxt.setOnClickListener {
                callback.onItemClickListener(item)
            }

        }
    }


    fun setItems(newItems: List<ServicesDTO> = mutableListOf()) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}