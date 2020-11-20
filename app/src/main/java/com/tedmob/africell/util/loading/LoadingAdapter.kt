package com.tedmob.africell.util.loading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.africell.R
import com.tedmob.africell.ui.blocks.LoadingView
import kotlinx.android.synthetic.main.loading_item_view.view.*

class LoadingAdapter : RecyclerView.Adapter<LoadingAdapter.LoadingHolder>() {

    sealed class State {
        object Loading : State()
        data class Error(val message: String, val retryLabel: String, val retryAction: (() -> Unit)?) : State()
        object Success : State()
    }

    var state: State = State.Loading
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }


    class LoadingHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loadingView: LoadingView = view.loadingView
    }

    override fun getItemCount(): Int = if (state == State.Success) 0 else 1

    override fun getItemViewType(position: Int): Int = 1//not 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingHolder {
        return LoadingHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: LoadingHolder, position: Int) {
        state.let { tempState ->
            when (tempState) {
                is State.Loading -> {
                    holder.loadingView.run {
                        loading(true)
                    }
                    Unit
                }

                is State.Error -> {
                    holder.loadingView.run {
                        loading(false)
                            .displayImage(false)
                            .displayMessage(true)
                            .message(tempState.message)
                            .displayButton(tempState.retryAction != null)
                            .buttonText(tempState.retryLabel)
                            .buttonClickListener(View.OnClickListener { tempState.retryAction?.invoke() })
                    }
                    Unit
                }
            }
        }
    }
}