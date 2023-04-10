package com.africell.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentVerificationBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.africell.africell.util.navigation.runIfFrom

class VerifyPinFragment : BaseVBFragment<FragmentVerificationBinding>() {

    val isReset by lazy {
        arguments?.getBoolean(MobileNumberFragment.IS_RESET)
            ?: throw IllegalArgumentException("required Type arguments")
    }

    private val viewModel by provideViewModel<VerifyPinViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentVerificationBinding::inflate, false, ToolbarDefaultBinding::inflate)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.title = getString(R.string.verification)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        withVBAvailable {
            verifyButton.setOnClickListener { viewModel.verifyPin(pinLayout.getText()) }
        }
        bindData()
    }


    private fun bindData() {
        observeResource(viewModel.verifyData) {
            if (isReset) {
                findNavController().runIfFrom(R.id.verifyPinFragment) {
                    navigate(R.id.action_verifyPinFragment_to_setPasswordFragment)
                }
            } else {
                findNavController().runIfFrom(R.id.verifyPinFragment) {
                    navigate(R.id.action_verifyPinFragment_to_registerFragment)
                }
            }
        }
    }

}
