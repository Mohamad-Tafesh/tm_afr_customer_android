package com.tedmob.afrimoney.features.locate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.afrimoney.data.entity.LocateUsEntry
import com.tedmob.afrimoney.databinding.ItemLocateListBinding

class LocateUsListAdapter(
    private val onCallUsClick: (entry: LocateUsEntry) -> Unit,
    private val onDirectionsClick: (entry: LocateUsEntry) -> Unit,
) : ListAdapter<LocateUsEntry, LocateUsListAdapter.LocateUsHolder>(getDiffUtil()) {

    companion object {
        fun getDiffUtil() = object : DiffUtil.ItemCallback<LocateUsEntry>() {

            override fun areItemsTheSame(oldItem: LocateUsEntry, newItem: LocateUsEntry): Boolean =
                oldItem.coordinates == newItem.coordinates

            override fun areContentsTheSame(oldItem: LocateUsEntry, newItem: LocateUsEntry): Boolean =
                oldItem == newItem
        }
    }


    class LocateUsHolder(private val binding: ItemLocateListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: LocateUsEntry,
            onCallUsClick: (entry: LocateUsEntry) -> Unit,
            onDirectionsClick: (entry: LocateUsEntry) -> Unit,
        ) {
            binding.run {
                distanceText.text = item.distanceFormatted
                titleText.text = item.title
                addressText.text = item.address

                callUsText.setOnClickListener { onCallUsClick(item) }
                directionsText.setOnClickListener { onDirectionsClick(item) }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocateUsHolder {
        return LocateUsHolder(
            ItemLocateListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocateUsHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onCallUsClick, onDirectionsClick)
    }
}