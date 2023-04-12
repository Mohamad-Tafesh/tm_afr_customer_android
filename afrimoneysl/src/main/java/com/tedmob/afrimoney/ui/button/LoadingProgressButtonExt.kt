package com.tedmob.afrimoney.ui.button

import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseFragment
import com.tedmob.afrimoney.data.Resource
import java.lang.ref.WeakReference

val viewsObserved = mutableMapOf<Int, LifecycleObserver>()

fun LifecycleOwner.bindProgressButton(button: LoadingProgressButton) {
    val oldObserver = viewsObserved[button.id]
    oldObserver?.let { lifecycle.removeObserver(it) }

    viewsObserved[button.id] = ProgressButtonHolder(
        WeakReference(button)
    ).also {
        lifecycle.addObserver(it)
    }
}

fun <T : Any, L : LiveData<Resource<T>>> L.observeInView(
    fragment: BaseFragment,
    loadingProgressButton: LoadingProgressButton,
    observerBlock: LiveDataObserver.FragmentLoadingButton<T>.() -> Unit
) {
    val observer =
        LiveDataObserver.FragmentLoadingButton<T>(loadingProgressButton).apply(observerBlock)

    fragment.viewLifecycleOwner.bindProgressButton(loadingProgressButton)

    observe(fragment.viewLifecycleOwner, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    observer.showLoading()
                }

                is Resource.Success -> {
                    val overrideAction = observer.overrideDefaultOnSuccess

                    if (overrideAction != null) {
                        overrideAction(resource.data)
                    } else {
                        observer.onSuccessWhenLoading(resource.data)
                        observer.hideLoading(resource)
                        observer.onSuccessPreMorph(resource.data)
                        loadingProgressButton.postDelayed(
                            { observer.onSuccess(resource.data) },
                            400L
                        )
                    }
                }

                is Resource.Error -> {
                    val overrideAction = observer.overrideDefaultOnError

                    if (overrideAction != null) {
                        overrideAction(resource.message, resource.action)
                    } else {
                        observer.onErrorWhenLoading(resource.message, resource.action)
                        observer.hideLoading(resource)
                        loadingProgressButton.postDelayed(
                            {
                                if (resource.action == null) {
                                    fragment.showMessage(resource.message)
                                } else {
                                    fragment.showMessageWithAction(
                                        resource.message,
                                        fragment.getString(R.string.retry),
                                        resource.action
                                    )
                                }

                                observer.onError(resource.message, resource.action)
                            },
                            400L
                        )
                    }
                }
            }
        }
    })
}


open class LiveDataObserver<T> {
    open var overrideDefaultOnLoading: (() -> Unit)? = null
    open var overrideDefaultOnSuccess: ((data: T) -> Unit)? = null
    open var overrideDefaultOnError: ((message: CharSequence, action: (() -> Unit)?) -> Unit)? = null

    open var showLoading: () -> Unit = {}
    open var hideLoading: (resource: Resource<T>) -> Unit = {}

    open var onLoading: () -> Unit = {}
    open var onSuccessWhenLoading: (data: T) -> Unit = {}
    open var onSuccessPreMorph: (data: T) -> Unit = {}
    open var onSuccess: (data: T) -> Unit = {}
    open var onErrorWhenLoading: (message: CharSequence, action: (() -> Unit)?) -> Unit = { _, _ -> }
    open var onError: (message: CharSequence, action: (() -> Unit)?) -> Unit = { _, _ -> }


    open class FragmentLoadingButton<T>(loadingProgressButton: LoadingProgressButton) :
        LiveDataObserver<T>() {

        private val buttonRef = WeakReference(loadingProgressButton)
        val startingText: CharSequence? = loadingProgressButton.text

        val button: LoadingProgressButton? get() = buttonRef.get()


        override var showLoading: () -> Unit = { button?.showProgress() }

        override var hideLoading: (resource: Resource<T>) -> Unit =
            { button?.hideProgress(startingText) }

        override var onLoading: () -> Unit = { showLoading() }
    }
}


private class ProgressButtonHolder(private val button: WeakReference<LoadingProgressButton>) :
    LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        button.get()?.cancelAnimations()
    }
}