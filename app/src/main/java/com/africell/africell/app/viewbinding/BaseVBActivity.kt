package com.africell.africell.app.viewbinding

import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.africell.africell.app.BaseActivity
import com.africell.africell.data.repository.domain.SessionRepository
import javax.inject.Inject

abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    @Inject
    lateinit var session: SessionRepository

    var binding: VB? = null
        private set

    fun requireBinding(): VB = requireNotNull(binding) { "View Binding required and was not found." }

    var toolbarBinding: ViewBinding? = null
    inline fun <TB : ViewBinding?> getToolbarBindingAs(): TB? = toolbarBinding as? TB?


    fun setContent(viewBinding: VB) {
        binding = viewBinding
        setContentView(viewBinding.root)
    }

    fun setContent(
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
        setContent(
            viewBindingProvider(layoutInflater),
            wrapLoading,
            toolbarLayoutBindingProvider?.invoke(layoutInflater)
        )
    }


    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}


inline fun <VB : ViewBinding> BaseVBActivity<VB>.withVBAvailable(block: VB.() -> Unit) = binding?.block()