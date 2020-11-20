package com.tedmob.africell.ui.viewmodel


import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseActivity
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.Resource
import com.tedmob.africell.ui.button.LoadingProgressButton
import com.tedmob.africell.ui.button.bindProgressButton

inline fun <reified T : ViewModel> ComponentActivity.provideViewModel(
    noinline keyProducer: (() -> String?)? = null,
    noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory }
): Lazy<T> {
    val canonicalName: String = T::class.java.canonicalName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

    return CustomViewModelLazy(
        T::class,
        keyProducer?.let { { "$canonicalName:${it.invoke()}" } },
        { viewModelStore },
        factoryProducer
    )
}

inline fun <reified T : ViewModel> Fragment.provideViewModel(
    noinline keyProducer: (() -> String?)? = null,
    noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory }
): Lazy<T> {
    val canonicalName: String = T::class.java.canonicalName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

    return CustomViewModelLazy(
        T::class,
        keyProducer?.let { { "$canonicalName:${it.invoke()}" } },
        { viewModelStore },
        factoryProducer
    )
}

inline fun <reified T : ViewModel> Fragment.provideParentViewModel(
    noinline keyProducer: (() -> String?)? = null,
    noinline factoryProducer: () -> ViewModelProvider.Factory = { defaultViewModelProviderFactory }
): Lazy<T> {
    val canonicalName: String = T::class.java.canonicalName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

    return CustomViewModelLazy(
        T::class,
        keyProducer?.let { { "$canonicalName:${it.invoke()}" } },
        { requireParentFragment().viewModelStore },
        factoryProducer
    )
}

inline fun <reified T : ViewModel> Fragment.provideActivityViewModel(
    noinline keyProducer: (() -> String?)? = null,
    noinline factoryProducer: () -> ViewModelProvider.Factory = { requireActivity().defaultViewModelProviderFactory }
): Lazy<T> {
    val canonicalName: String = T::class.java.canonicalName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

    return CustomViewModelLazy(
        T::class,
        keyProducer?.let { { "$canonicalName:${it.invoke()}" } },
        { requireActivity().viewModelStore },
        factoryProducer
    )
}

inline fun <reified T : ViewModel> Fragment.provideNavGraphViewModel(
    @IdRes navGraphId: Int,
    noinline keyProducer: (() -> String?)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<T> {
    val canonicalName: String = T::class.java.canonicalName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels")

    //val backStackEntry: NavBackStackEntry by lazy { findNavController().getBackStackEntry(navGraphId) }

    return CustomViewModelLazy(
        T::class,
        keyProducer?.let { { "$canonicalName:${it.invoke()}" } },
        { /*backStackEntry*/findNavController().getBackStackEntry(navGraphId).viewModelStore },
        {
            factoryProducer?.invoke()
                ?: /*backStackEntry*/findNavController().getBackStackEntry(navGraphId).defaultViewModelProviderFactory
        }
    )
}

//LiveData:
fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L?, body: (T?) -> Unit) {
    liveData?.observe(this, Observer(body))
}

fun <T : Any, L : LiveData<T>> BaseFragment.observe(liveData: L?, body: (T?) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer(body))
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observeNotNull(liveData: L?, body: (T) -> Unit) {
    liveData?.observe(this, Observer { it?.let(body) })
}

fun <T : Any, L : LiveData<T>> BaseFragment.observeNotNull(liveData: L?, body: (T?) -> Unit) {
    liveData?.observe(viewLifecycleOwner, Observer { it?.let(body) })
}


//LiveData with Resource:
fun <T : Any, L : LiveData<Resource<T>>> BaseFragment.observeResource(
    liveData: L?,
    body: (T) -> Unit
) {
    liveData?.observe(viewLifecycleOwner, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    body.invoke(resource.data)
                }
                is Resource.Error -> {
                    if (resource.action == null) {
                        showMessage(resource.message)
                    } else {
                        showMessageWithAction(
                            resource.message,
                            getString(R.string.retry),
                            resource.action
                        )
                    }
                }
            }
        }
    })
}

fun <T : Any, L : LiveData<Resource<T>>> BaseFragment.observeResourceProgress(
    liveData: L?,
    body: (T) -> Unit
) {
    liveData?.observe(viewLifecycleOwner, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> showProgressDialog(getString(R.string.loading_))
                is Resource.Success -> {
                    hideProgressDialog()
                    body.invoke(resource.data)
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    if (resource.action == null) {
                        showMessage(resource.message)
                    } else {
                        showMessageWithAction(
                            resource.message,
                            getString(R.string.retry),
                            resource.action
                        )
                    }
                }
            }
        }
    })
}

fun <T : Any, L : LiveData<Resource<T>>> BaseFragment.observeResourceFromButton(
    liveData: L?,
    @IdRes buttonRes: Int,
    body: (T) -> Unit
) {
    val loadingProgressButton = view?.findViewById<LoadingProgressButton>(buttonRes)
    val currentText = loadingProgressButton?.text?.toString()

    loadingProgressButton?.let { viewLifecycleOwner.bindProgressButton(it) }

    liveData?.observe(viewLifecycleOwner, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> loadingProgressButton?.showProgress()
                is Resource.Success -> {
                    loadingProgressButton?.hideProgress(currentText)

                    loadingProgressButton?.postDelayed(
                        {
                            body.invoke(resource.data)
                        },
                        400L
                    )
                }
                is Resource.Error -> {
                    loadingProgressButton?.hideProgress(currentText)//TODO failure text, or retry?

                    loadingProgressButton?.postDelayed(
                        {
                            if (resource.action == null) {
                                showMessage(resource.message)
                            } else {
                                showMessageWithAction(
                                    resource.message,
                                    getString(R.string.retry),
                                    resource.action
                                )
                            }
                        },
                        400L
                    )
                }
            }
        }
    })
}

fun <T : Any, L : LiveData<Resource<T>>> BaseFragment.observeResourceInline(
    liveData: L?,
    body: (T) -> Unit
) {
    liveData?.observe(viewLifecycleOwner, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showInlineLoading()
                }
                is Resource.Success -> {
                    showContent()
                    body.invoke(resource.data)
                }
                is Resource.Error -> {
                    if (resource.action == null) {
                        showInlineMessage(resource.message)
                    } else {
                        showInlineMessageWithAction(
                            resource.message,
                            getString(R.string.retry),
                            resource.action
                        )
                    }
                }
            }
        }
    })
}

fun <T : Any, L : LiveData<Resource<T>>> BaseActivity.observeResource(
    liveData: L?,
    body: (T) -> Unit
) {
    liveData?.observe(this, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    body.invoke(resource.data)
                }
                is Resource.Error -> {
                    if (resource.action == null) {
                        showMessage(resource.message)
                    } else {
                        showMessageWithAction(
                            resource.message,
                            getString(R.string.retry),
                            resource.action
                        )
                    }
                }
            }
        }
    })
}

fun <T : Any, L : LiveData<Resource<T>>> BaseActivity.observeResourceProgress(
    liveData: L?,
    body: (T) -> Unit
) {
    liveData?.observe(this, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> showProgressDialog(getString(R.string.loading_))
                is Resource.Success -> {
                    hideProgressDialog()
                    body.invoke(resource.data)
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    if (resource.action == null) {
                        showMessage(resource.message)
                    } else {
                        showMessageWithAction(
                            resource.message,
                            getString(R.string.retry),
                            resource.action
                        )
                    }
                }
            }
        }
    })
}

fun <T : Any, L : LiveData<Resource<T>>> BaseActivity.observeResourceInline(
    liveData: L?,
    body: (T) -> Unit
) {
    liveData?.observe(this, Observer {
        it?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showInlineLoading()
                }
                is Resource.Success -> {
                    showContent()
                    body.invoke(resource.data)
                }
                is Resource.Error -> {
                    if (resource.action == null) {
                        showInlineMessage(resource.message)
                    } else {
                        showInlineMessageWithAction(
                            resource.message,
                            getString(R.string.retry),
                            resource.action
                        )
                    }
                }
            }
        }
    })
}
