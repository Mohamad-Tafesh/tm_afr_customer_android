package com.africell.africell.features.faq

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.africell.africell.R
import com.africell.africell.ui.inflate
import kotlinx.android.synthetic.main.item_faq.view.*

class FaqAdapter() :
    ListAdapter<FaqItem, FaqAdapter.FaqHolder>(getDiffCallback()) {

    companion object {
        private const val ANIMATION_DURATION = 300L

        fun getDiffCallback(): DiffUtil.ItemCallback<FaqItem> =
            object : DiffUtil.ItemCallback<FaqItem>() {
                override fun areItemsTheSame(oldItem: FaqItem, newItem: FaqItem): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: FaqItem, newItem: FaqItem): Boolean =
                    oldItem == newItem
            }
    }


    private val isExpandedStates: MutableMap<Int, Boolean> = mutableMapOf()


    class FaqHolder(view: View) : RecyclerView.ViewHolder(view) {
        //fixme below assignments are used in case you wanted to wrap the question or the answer with a parent layout (to keep click and animation behavior)
        val questionLayout = view.questionText
        val answerLayout = view.answerText

        fun bind(item: FaqItem, isExpandedStates: MutableMap<Int, Boolean>) {
            itemView.run {
                val answer = item.response + "\n"
                questionText.text = item.question
                answerText.text = answer

                questionLayout.isClickable = true
                if (isExpandedStates[adapterPosition] == true) {
                    answerLayout.isVisible = true

                    //either keep the rotation, or set the new image
                    //arrow.rotation = 180f
                    arrow.rotation = 0f
                    arrow.setImageResource(R.mipmap.icon_arrow_pink_up)
                } else {
                    answerLayout.isVisible = false

                    //either keep the rotation, or set the new image
                    //arrow.rotation = 0f
                    arrow.rotation = 0f
                    arrow.setImageResource(R.mipmap.icon_arrow_pink_down)
                }
                arrow.setOnClickListener {
                    if (isExpandedStates[adapterPosition] == true) {
                        questionLayout.isClickable = false
                        arrow.isClickable = false
                        isExpandedStates[adapterPosition] = false
                        answerLayout.collapse {
                            questionLayout.isClickable = true
                            arrow.isClickable = true
                        }
                        arrow.rotateBackUp()
                    } else {
                        questionLayout.isClickable = false
                        arrow.isClickable = false
                        isExpandedStates[adapterPosition] = true
                        answerLayout.expand {
                            questionLayout.isClickable = true
                            arrow.isClickable = true
                        }
                        arrow.rotateDown()
                    }
                }
                layout.setOnClickListener {
                    if (isExpandedStates[adapterPosition] == true) {
                        questionLayout.isClickable = false
                        arrow.isClickable = false
                        isExpandedStates[adapterPosition] = false
                        answerLayout.collapse {
                            questionLayout.isClickable = true
                            arrow.isClickable = true
                        }
                        arrow.rotateBackUp()
                    } else {
                        questionLayout.isClickable = false
                        arrow.isClickable = false
                        isExpandedStates[adapterPosition] = true
                        answerLayout.expand {
                            questionLayout.isClickable = true
                            arrow.isClickable = true
                        }
                        arrow.rotateDown()
                    }
                }

            }
        }

        private fun View.expand(onFinish: () -> Unit) {
            val displayWidth = resources.displayMetrics.widthPixels
            val displayHeight = resources.displayMetrics.heightPixels
            measure(
                View.MeasureSpec.makeMeasureSpec(displayWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(displayHeight, View.MeasureSpec.AT_MOST)
            )
            val targetHeight = (measuredHeight * 1.2).toInt()
            layoutParams.height = 0
            isVisible = true

            ValueAnimator.ofInt(0, targetHeight)
                .setDuration(ANIMATION_DURATION)
                .apply {
                    addUpdateListener {
                        layoutParams.height = it.animatedValue as Int
                        requestLayout()
                    }

                    addListener(
                        onEnd = { onFinish() }
                    )
                }.start()
        }

        private fun View.collapse(onFinish: () -> Unit) {
            val displayWidth = resources.displayMetrics.widthPixels
            val displayHeight = resources.displayMetrics.heightPixels
            measure(
                View.MeasureSpec.makeMeasureSpec(displayWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(displayHeight, View.MeasureSpec.UNSPECIFIED)
            )
            ValueAnimator.ofInt(measuredHeight, 0)
                .setDuration(ANIMATION_DURATION)
                .apply {
                    addUpdateListener {
                        layoutParams.height = it.animatedValue as Int
                        requestLayout()
                    }

                    addListener(
                        onEnd = {
                            isVisible = false
                            onFinish()
                        }
                    )
                }.start()
        }

        private fun View.rotateDown() {
            animate()
                .rotation(-180f)
                .setDuration(ANIMATION_DURATION)
                .start()
        }

        private fun View.rotateBackUp() {
            animate()
                .rotation(0f)
                .setDuration(ANIMATION_DURATION)
                .start()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqHolder {
        val v = parent.inflate(R.layout.item_faq)
        return FaqHolder(v)
    }

    override fun onBindViewHolder(holder: FaqHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, isExpandedStates)
    }
}