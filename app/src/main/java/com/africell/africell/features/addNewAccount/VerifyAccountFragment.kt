package com.africell.africell.features.addNewAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import kotlinx.android.synthetic.main.fragment_verification.*

class VerifyAccountFragment : BaseFragment() {


    val msisdn by lazy {
        arguments?.getString(MSISDN_KEY)
            ?: throw IllegalArgumentException("required msisdn arguments")
    }

    companion object {
        const val MSISDN_KEY = "msisdn_key"
    }

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

        verifyButton.setOnClickListener { viewModel.verifyPin(msisdn,pinLayout.getText()) }
        bindData()
    }


    private fun bindData() {
        observeResource(viewModel.verifyData) {
            showMaterialMessageDialog(getString(R.string.successful),"Your Account has been added successfully", getString(R.string.close)) {
                activity?.finish()
            }
        }
    }

}
