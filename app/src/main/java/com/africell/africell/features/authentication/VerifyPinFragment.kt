package com.africell.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import kotlinx.android.synthetic.main.fragment_verification.*

class VerifyPinFragment : BaseFragment(){

    val isReset by lazy {
        arguments?.getBoolean(MobileNumberFragment.IS_RESET)
            ?: throw IllegalArgumentException("required Type arguments")
    }

    private val viewModel by provideViewModel<VerifyPinViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_verification, R.layout.toolbar_default, false)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.title = getString(R.string.verification)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        verifyButton.setOnClickListener { viewModel.verifyPin(pinLayout.getText()) }
        bindData()
    }



    private fun bindData() {
        observeResource(viewModel.verifyData) {
           if (isReset) {
                findNavController().navigate(R.id.action_verifyPinFragment_to_setPasswordFragment)
            } else {
                findNavController().navigate(R.id.action_verifyPinFragment_to_registerFragment)
            }
    }
    }

}
