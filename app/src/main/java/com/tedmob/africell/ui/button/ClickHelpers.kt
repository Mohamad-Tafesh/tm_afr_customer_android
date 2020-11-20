package com.tedmob.africell.ui.button

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun View.setDebouncedOnClickListener(
    debounceTimeMillis: Long = 200L,
    onClick: (View) -> Unit
) {

    val disposable = RxView.clicks(this)
        .debounce(debounceTimeMillis, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ onClick(this) }, {})

    addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View?) {
                if (!disposable.isDisposed) {
                    disposable.dispose()
                }
            }
        }
    )
}

inline fun View.setDebouncedOnClickListener(
    lifecycleOwner: LifecycleOwner,
    debounceTimeMillis: Long = 200L,
    noinline onClick: (View) -> Unit
) {
    setDebouncedOnClickListener(lifecycleOwner.lifecycle, debounceTimeMillis, onClick)
}

fun View.setDebouncedOnClickListener(
    lifecycle: Lifecycle,
    debounceTimeMillis: Long = 200L,
    onClick: (View) -> Unit
) {
    val disposable = RxView.clicks(this)
        .debounce(debounceTimeMillis, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ onClick(this) }, {})

    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyed() {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    })
}

