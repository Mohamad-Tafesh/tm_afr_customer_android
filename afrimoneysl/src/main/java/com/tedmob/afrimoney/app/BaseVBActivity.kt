package com.tedmob.afrimoney.app

import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding

abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    var binding: VB? = null
    fun requireBinding(): VB = requireNotNull(binding) { "View Binding required and was not found." }

    var toolbarBinding: ViewBinding? = null
    inline fun <TB : ViewBinding?> getToolbarBindingAs(): TB? = toolbarBinding as? TB?


    inline fun setContent(viewBinding: VB) {
        binding = viewBinding
        setContentView(viewBinding.root)
    }

    inline fun setContent(
        viewBinding: VB,
        wrapLoading: Boolean,
        toolbarLayoutBinding: ViewBinding? = null
    ) {
        toolbarBinding = toolbarLayoutBinding
        binding = viewBinding
        setContentView(viewBinding.root, wrapLoading, toolbarLayoutBinding?.root)
    }

    inline fun setContent(viewBindingProvider: (inflater: LayoutInflater) -> VB) {
        setContent(viewBindingProvider(layoutInflater))
    }

    inline fun setContent(
        viewBindingProvider: (inflater: LayoutInflater) -> VB,
        wrapLoading: Boolean,
        noinline toolbarLayoutBindingProvider: ((inflater: LayoutInflater) -> ViewBinding)? = null
    ) {
        setContent(viewBindingProvider(layoutInflater), wrapLoading, toolbarLayoutBindingProvider?.invoke(layoutInflater))
    }


    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}


inline fun <VB : ViewBinding> BaseVBActivity<VB>.withVBAvailable(block: VB.() -> Unit) = binding?.block()