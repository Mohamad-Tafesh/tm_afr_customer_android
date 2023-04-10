package com.africell.africell.features.dataCalculator

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.data.api.dto.DataCalculatorDTO
import com.africell.africell.databinding.RowDataCalculatorBinding


class DataCalculatorAdapter(
    private var items: List<DataCalculatorDTO.DataCalculator>,
    private var seekbarValueItems: HashMap<String, Double>,
    private val callback: Callback?,
) : RecyclerView.Adapter<DataCalculatorAdapter.HomeItemHolder>() {
    interface Callback {
        fun onItemChangedListener(seekbarValueItems: HashMap<String, Double>)
    }

    class HomeItemHolder(val viewBinding: RowDataCalculatorBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemHolder {
        val itemView = RowDataCalculatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeItemHolder, position: Int) {
        val item = items[position]
        holder.viewBinding.run {
            titleTxt.text = item.name
            seekbar.max = item.maximumValue?.toDoubleOrNull()?.toInt() ?: 100
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                seekbar.min = item.minimumValue?.toDoubleOrNull()?.toInt() ?: 0
            }
            seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val progressPerUnit = progress.toDouble() * (item.costPerUnit?.toDouble() ?: 0.0)
                    seekbarValueItems.put(item.idDataCalculator, progressPerUnit)
                    callback?.onItemChangedListener(seekbarValueItems)
                    valueTxt.text = progress.toString() + item.variableUnit
                }
            })



            valueTxt.text = item.minimumValue + item.variableUnit
            if (position % 2 == 0) {
                seekbar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.yellow))
                seekbar.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.yellow))

            } else {
                seekbar.progressTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.purple))
                seekbar.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.purple))


            }
        }
    }


    fun setItems(
        newItems: List<DataCalculatorDTO.DataCalculator> = mutableListOf(),
        seekbarValueItems: HashMap<String, Double>
    ) {
        items = newItems.toMutableList()
        this.seekbarValueItems = seekbarValueItems
        notifyDataSetChanged()
    }
}