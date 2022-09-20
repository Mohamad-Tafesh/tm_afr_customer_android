package com.africell.africell.features.afrimoneyMerchantPaymnet

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.dto.WalletDTO
import com.africell.africell.data.api.requests.afrimoney.MerchantPayRequest
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import kotlinx.android.synthetic.main.fragment_afrimoney_activate_bundle.*
import kotlinx.android.synthetic.main.fragment_afrimoney_merchant_pay.*
import kotlinx.android.synthetic.main.fragment_afrimoney_merchant_pay.closeIcon
import kotlinx.android.synthetic.main.fragment_afrimoney_merchant_pay.pinCodeLayout
import kotlinx.android.synthetic.main.fragment_afrimoney_merchant_pay.selectWalletLayout
import kotlinx.android.synthetic.main.fragment_afrimoney_merchant_pay.submitBtn
import javax.inject.Inject


class AfrimoneyMerchantPayFragment : BaseFragment(), Liv.Action {
    private var liv: Liv? = null



    @Inject
    lateinit var sessionRepository: SessionRepository

    private val viewModel by provideViewModel<AfrimoneyMerchantPayViewModel> { viewModelFactory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_afrimoney_merchant_pay, 0, true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(BuildConfig.FLAVOR == "sl") {
            pinCodeLayout.editText?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            pinCodeLayout.editText?.transformationMethod = PasswordTransformationMethod.getInstance();
        }
        liv = initLiv()
        liv?.start()
        submitBtn.setOnClickListener {
            activity?.hideKeyboard()
            liv?.submitWhenValid()
        }
        bindData()

        setupUI()
        closeIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun setupUI() {
        submitBtn.setBackgroundColor(resources.getColor(R.color.purple))
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val builder = Liv.Builder()
        builder
            .add(selectWalletLayout, notEmptyRule)
        return builder
            .submitAction(this)
            .build()

    }

    private fun bindData() {
        viewModel.getData()
        observeResourceInline(viewModel.data) { wallet ->

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.textview_spinner, wallet)
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            selectWalletLayout.adapter = arrayAdapter
            if(BuildConfig.FLAVOR == "sl") {
                selectWalletLayout.selection = 0

            }
        }

        observeResource(viewModel.requestData) {
            showMaterialMessageDialog(getString(R.string.successful),it.resultText.orEmpty(), getString(R.string.close)) {
                //  this@AfrimoneyP2PFragment.dismiss()
                findNavController().popBackStack()
            }
        }

    }


    override fun performAction() {
        val wallet = (selectWalletLayout.selectedItem as? WalletDTO)?.name
        val request = MerchantPayRequest(wallet, merchantLayout.getText(), pinCodeLayout.getText(), amountLayout.getText())
        viewModel.submitRequest(request)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }


}

