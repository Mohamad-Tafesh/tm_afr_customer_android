package com.africell.africell.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlin.reflect.KClass

class CustomViewModelLazy<VM : ViewModel>(
    private val viewModelClass: KClass<VM>,
    private val keyProducer: (() -> String?)?,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()

                run {
                    keyProducer?.invoke()?.let {
                        ViewModelProvider(store, factory)[it, viewModelClass.java]
                    }
                        ?: ViewModelProvider(store, factory)[viewModelClass.java]
                }.also {
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized() = cached != null
}