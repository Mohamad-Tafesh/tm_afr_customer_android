package com.tedmob.afrimoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class CustomViewModelLazy<VM : ViewModel>(
    private val viewModelClass: KClass<VM>,
    private val keyProducer: (() -> String?)?,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory,
    private val extrasProducer: () -> CreationExtras = { CreationExtras.Empty },
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                val provider = ViewModelProvider(store, factory, extrasProducer())

                run {
                    keyProducer?.invoke()?.let { provider[it, viewModelClass.java] }
                        ?: provider[viewModelClass.java]
                }.also {
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized() = cached != null
}