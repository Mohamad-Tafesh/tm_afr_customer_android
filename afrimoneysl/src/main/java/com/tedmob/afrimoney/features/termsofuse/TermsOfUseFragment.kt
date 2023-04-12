package com.tedmob.afrimoney.features.termsofuse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentTermsOfUseBinding
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsOfUseFragment : BaseVBFragment<FragmentTermsOfUseBinding>() {

    private val viewModel by provideViewModel<TermsOfUseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun configureToolbar() {
        actionbar?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentTermsOfUseBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()

        viewModel.getData()
    }

    private fun bindData() {
        observeResourceInline(viewModel.content) {
            withVBAvailable {
                setupTermsText(it.termsText)
                setupPolicyText(it.policyText)
            }
        }
    }

    private fun FragmentTermsOfUseBinding.setupTermsText(text: String) {
        tvTermsText.text = text.parseAsHtml()
    }

    private fun FragmentTermsOfUseBinding.setupPolicyText(text: String) {
        tvPolicyText.text = text.parseAsHtml()
    }

}