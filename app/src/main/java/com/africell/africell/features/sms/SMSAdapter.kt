package com.africell.africell.features.sms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.api.ApiContract.Params.SMS_FREE_MAX
import com.africell.africell.databinding.RowSmsBinding

class SMSAdapter(
    private var count: Int
) : RecyclerView.Adapter<SMSAdapter.HomeItemHolder>() {

    class HomeItemHolder(val viewBinding: RowSmsBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = SMS_FREE_MAX

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val itemViewBinding = RowSmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val displayMetrics = parent.context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val layoutParams = itemViewBinding.root.layoutParams
        val padding = parent.context.resources.getDimensionPixelSize(R.dimen.spacing_small)
        layoutParams.width = (width - padding) / SMS_FREE_MAX
        layoutParams.height = (width - padding) / SMS_FREE_MAX
        itemViewBinding.root.layoutParams = layoutParams

        return HomeItemHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        holder.viewBinding.run {
            val openPosition = SMS_FREE_MAX - count;

            if (position == openPosition) {
                backgroundId.setBackgroundResource(R.drawable.circle_bgd_transparent)
            } else backgroundId.setBackgroundColor(root.resources.getColor(R.color.transparent))

            textItem.alpha = if (position < openPosition) {
                0.5f
            } else 1f

            textItem.text = if (position == openPosition) "Open"
            else if (position < openPosition) "Sent" else "New"
        }
    }


    fun setItems(count: Int) {
        this.count = count
        notifyDataSetChanged()
    }
}