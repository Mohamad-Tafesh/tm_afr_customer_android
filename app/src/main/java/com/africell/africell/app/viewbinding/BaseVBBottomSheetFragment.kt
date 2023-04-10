package com.africell.africell.app.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.africell.africell.app.BaseBottomSheetFragment

abstract class BaseVBBottomSheetFragment<VB: ViewBinding> : BaseBottomSheetFragment() {
    var binding: VB? = null
        private set

    fun requireBinding(): VB = requireNotNull(binding) { "View Binding required and was not found." }

    var toolbarBinding: ViewBinding? = null
    fun <TB : ViewBinding?> getToolbarBindingAs(): TB? = toolbarBinding as? TB?

    protected var hasLoading: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return onCreateViewBinding(inflater, container, savedInstanceState).let {
            binding = it
            getCombinedRootFromBindings(it)
        }
    }

    protected fun createViewBinding(
        container: ViewGroup?,
        viewBindingProvider: ((inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> VB)?,
        hasLoading: Boolean = false,
        toolbarBindingProvider: ((inflater: LayoutInflater) -> ViewBinding)? = null
    ): View? {
        binding = viewBindingProvider?.invoke(layoutInflater, container, false)
        this.hasLoading = hasLoading
        toolbarBinding = toolbarBindingProvider?.invoke(layoutInflater)

        return getCombinedRootFromBindings(binding)
    }

    open fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB? =
        null

    private inline fun getCombinedRootFromBindings(rootBinding: VB?): View? =
        rootBinding?.root
            ?.let {
                wrap(it.context, it, hasLoading, toolbarBinding?.root)
            }


    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}


inline fun <VB : ViewBinding> BaseVBBottomSheetFragment<VB>.withVBAvailable(block: VB.() -> Unit) = binding?.block()