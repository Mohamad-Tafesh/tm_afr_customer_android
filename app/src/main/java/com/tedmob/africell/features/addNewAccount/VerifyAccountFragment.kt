package com.tedmob.africell.features.addNewAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import kotlinx.android.synthetic.main.fragment_verification.*

class VerifyAccountFragment : BaseFragment() {


    private val viewModel by provideViewModel<AddAccountViewModel> { viewModelFactory }

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
            showMaterialMessageDialog("Your Account has been added successfully",getString(R.string.close)) {
              activity?.finish()
            }
        }
    }

}
