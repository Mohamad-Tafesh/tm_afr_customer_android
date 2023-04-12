package com.tedmob.afrimoney.ui.viewmodel

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.BaseFragment
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.ui.button.LiveDataObserver
import com.tedmob.afrimoney.ui.button.LoadingProgressButton
import com.tedmob.afrimoney.ui.button.bindProgressButton

inline fun BaseFragment.observeTransactionSubmit(
    liveData: LiveData<Resource<SubmitResult>>,
    loadingProgressButton: LoadingProgressButton,
    message: String? = null,
    crossinline afterSuccess: () -> Unit = {}
) {
    val observer =
        LiveDataObserver.FragmentLoadingButton<SubmitResult>(loadingProgressButton).apply {
            onError = { message, action ->
                showMaterialMessageDialog(
                    message,
                    callback = action
                )
            }

            onSuccess = {
                when (it) {
                    is SubmitResult.Failure -> {
                        showTransactionFailure(it.message, null)
                    }
                    is SubmitResult.Success -> {
                        showTransactionSuccess(message ?: it.message) { afterSuccess() }
                    }
                }
            }
        }

    viewLifecycleOwner.bindProgressButton(loadingProgressButton)

    liveData.observe(viewLifecycleOwner) {
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
                            when (loadingProgressButton.state) {
                                LoadingProgressButton.State.PROGRESS, LoadingProgressButton.State.IDLE_TO_PROGRESS -> 400L
                                else -> 200L
                            }
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
                            { observer.onError(resource.message, resource.action) },
                            when (loadingProgressButton.state) {
                                LoadingProgressButton.State.PROGRESS, LoadingProgressButton.State.IDLE_TO_PROGRESS -> 400L
                                else -> 200L
                            }
                        )
                    }
                }
            }
        }
    }
}