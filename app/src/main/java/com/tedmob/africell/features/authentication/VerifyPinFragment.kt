package com.tedmob.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import kotlinx.android.synthetic.main.fragment_verification.*
import javax.inject.Inject

class VerifyPinFragment : BaseFragment(){

    val isReset by lazy {
        arguments?.getBoolean(MobileNumberFragment.IS_RESET)
            ?: throw IllegalArgumentException("required Type arguments")
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<VerifyPinViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_verification, R.layout.toolbar_default, false)
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
