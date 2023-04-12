package com.tedmob.afrimoney.features.faq

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.databinding.ItemFaqBinding


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


    class FaqHolder(private val view: ItemFaqBinding) : RecyclerView.ViewHolder(view.root) {
        //fixme below assignments are used in case you wanted to wrap the question or the answer with a parent layout (to keep click and animation behavior)
        val questionLayout = view.questionText
        val answerLayout = view.answerText

        fun bind(item: FaqItem, isExpandedStates: MutableMap<Int, Boolean>) {
            view.run {
                questionText.text = item.question
                answerText.text = item.answer

                questionLayout.isClickable = true
                if (isExpandedStates[adapterPosition] == true) {
                    answerLayout.isVisible = true

                    //either keep the rotation, or set the new image
                    //arrow.rotation = 180f
                    arrow.rotation = 0f
                    arrow.setImageResource(R.mipmap.ic_dropdown_arrow_up)
                } else {
                    answerLayout.isVisible = false

                    //either keep the rotation, or set the new image
                    //arrow.rotation = 0f
                    arrow.rotation = 0f
                    arrow.setImageResource(R.mipmap.ic_dropdown_arrow_down)
                }

                questionLayout.setOnClickListener {
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
            }
        }

        private fun View.expand(onFinish: () -> Unit) {
            val displayWidth = resources.displayMetrics.widthPixels
            measure(
                View.MeasureSpec.makeMeasureSpec(displayWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val targetHeight = measuredHeight
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
            measure(
                View.MeasureSpec.makeMeasureSpec(displayWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
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

        /*(v as? ViewGroup?)?.layoutTransition = LayoutTransition().apply {
            setDuration(LayoutTransition.APPEARING, 100L)
            getAnimator(LayoutTransition.APPEARING).run {
                duration = 100L
                startDelay = 100L
            }
            setDuration(LayoutTransition.CHANGE_APPEARING, 100L)
            getAnimator(LayoutTransition.CHANGE_APPEARING).run {
                duration = 100L
                startDelay = 0L
            }
            setDuration(LayoutTransition.DISAPPEARING, 100L)
            getAnimator(LayoutTransition.DISAPPEARING).run {
                duration = 100L
                startDelay = 0L
            }
            setDuration(LayoutTransition.CHANGE_DISAPPEARING, 100L)
            getAnimator(LayoutTransition.CHANGE_DISAPPEARING).run {
                duration = 100L
                startDelay = 100L
            }
        }*/
        return FaqHolder(ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FaqHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item, isExpandedStates)
    }
}