package com.africell.africell.features.afrimoneyMerchantPaymnet

import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.WalletDTO
import com.africell.africell.data.api.requests.afrimoney.MerchantPayRequest
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentAfrimoneyMerchantPayBinding
import com.africell.africell.ui.hideKeyboard
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.getText
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AfrimoneyMerchantPayFragment : BaseVBFragment<FragmentAfrimoneyMerchantPayBinding>(), Liv.Action {
    private var liv: Liv? = null


    @Inject
    lateinit var sessionRepository: SessionRepository

    private val viewModel by provideViewModel<AfrimoneyMerchantPayViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentAfrimoneyMerchantPayBinding::inflate, true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        withVBAvailable {
            if (BuildConfig.FLAVOR == "sl" || BuildConfig.FLAVOR == "gambia") {
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
    }


    private fun FragmentAfrimoneyMerchantPayBinding.setupUI() {
        submitBtn.setBackgroundColor(resources.getColor(R.color.purple))
    }

    private fun FragmentAfrimoneyMerchantPayBinding.initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val builder = Liv.Builder()
        builder
            .add(selectWalletLayout, notEmptyRule)
        return builder
            .submitAction(this@AfrimoneyMerchantPayFragment)
            .build()

    }

    private fun bindData() {
        viewModel.getData()
        observeResourceInline(viewModel.data) { wallet ->
            withVBAvailable {
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.textview_spinner, wallet)
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                selectWalletLayout.adapter = arrayAdapter
                if (BuildConfig.FLAVOR == "sl" || BuildConfig.FLAVOR == "gambia") {
                    selectWalletLayout.selection = 0

                }
            }
        }

        observeResource(viewModel.requestData) {
            showMaterialMessageDialog(
                getString(R.string.successful),
                it.resultText.orEmpty(),
                getString(R.string.close)
            ) {
                //  this@AfrimoneyP2PFragment.dismiss()
                findNavController().popBackStack()
            }
        }

    }


    override fun performAction() {
        withVBAvailable {
            val wallet = (selectWalletLayout.selectedItem as? WalletDTO)?.name
            val request =
                MerchantPayRequest(wallet, merchantLayout.getText(), pinCodeLayout.getText(), amountLayout.getText())
            viewModel.submitRequest(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }


}

