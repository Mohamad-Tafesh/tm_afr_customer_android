package com.africell.africell.app.viewbinding

import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.africell.africell.app.BaseActivity
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.ui.viewmodel.CustomViewModelLazy
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
            factoryProducer,
            { requireActivity().defaultViewModelCreationExtras },
        )
    }



    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}


inline fun <VB : ViewBinding> BaseVBActivity<VB>.withVBAvailable(block: VB.() -> Unit) = binding?.block()