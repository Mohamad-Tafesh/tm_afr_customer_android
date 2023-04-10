package com.africell.africell.features.addNewAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentVerificationBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText

class VerifyAccountFragment : BaseVBFragment<FragmentVerificationBinding>() {


    val msisdn by lazy {
        arguments?.getString(MSISDN_KEY)
            ?: throw IllegalArgumentException("required msisdn arguments")
    }

    companion object {
        const val MSISDN_KEY = "msisdn_key"
    }

    private val viewModel by provideViewModel<AddAccountViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentVerificationBinding::inflate, false, ToolbarDefaultBinding::inflate)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setDisplayHomeAsUpEnabled(false)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = getString(R.string.verification)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        withVBAvailable {
            verifyButton.setOnClickListener {
                withVBAvailable {
                    viewModel.verifyPin(msisdn, pinLayout.getText())
                }
            }
        }
        bindData()
    }


    private fun bindData() {
        observeResource(viewModel.verifyData) {
            showMaterialMessageDialog(
                getString(R.string.successful),
                getString(R.string.your_account_has_been_added_successfy),
                getString(R.string.close)
            ) {
                activity?.finish()
            }
        }
    }

}
