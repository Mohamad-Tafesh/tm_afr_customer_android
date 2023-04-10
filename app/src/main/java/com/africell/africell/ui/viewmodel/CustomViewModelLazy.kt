package com.africell.africell.ui.viewmodel

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
    private val creationExtrasProducer: () -> CreationExtras,
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                val creationExtras = creationExtrasProducer()

                run {
                    keyProducer?.invoke()?.let {
                        ViewModelProvider(store, factory, creationExtras)[it, viewModelClass.java]
                    }
                        ?: ViewModelProvider(store, factory, creationExtras)[viewModelClass.java]
                }.also {
                    cached = it
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized() = cached != null
}