package com.africell.africell.features.sms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.api.ApiContract.Params.SMS_FREE_MAX
import kotlinx.android.synthetic.main.row_sms.view.*

class SMSAdapter(
    private var count: Int
) : RecyclerView.Adapter<SMSAdapter.HomeItemHolder>() {

    class HomeItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = SMS_FREE_MAX

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_sms, parent, false)
        val displayMetrics = parent.context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val layoutParams = itemView.layoutParams
        val padding = parent.context.resources.getDimensionPixelSize(R.dimen.spacing_small)
        layoutParams.width = (width - padding) / SMS_FREE_MAX
          layoutParams.height= (width - padding) / SMS_FREE_MAX
        itemView.layoutParams = layoutParams

        return HomeItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        holder.itemView.run {
            val openPosition = SMS_FREE_MAX -count ;

            if (position == openPosition) {
                backgroundId.setBackgroundResource(R.drawable.circle_bgd_transparent)
            } else backgroundId.setBackgroundColor(resources.getColor(R.color.transparent))

            textItem.alpha= if (position < openPosition) {
                0.5f
            }else 1f

            textItem.text=if(position==openPosition) "Open"
            else if(position < openPosition) "Sent" else "New"
        }
    }


    fun setItems(count: Int) {
        this.count = count
        notifyDataSetChanged()
    }
}