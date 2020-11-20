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


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<VerifyPinViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_verification, 0, false)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.hide()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        verifyButton.setOnClickListener { viewModel.verifyPin(pinLayout.getText()) }
        bindData()
    }



    private fun bindData() {
        observeResource(viewModel.verifyData) {
           if (it.isNewUser == false) {
                findNavController().navigate(R.id.action_verifyPinFragment_to_mainActivity)
            } else {
                findNavController().navigate(R.id.action_verifyPinFragment_to_registerFragment)
            }


    }
    }

}
