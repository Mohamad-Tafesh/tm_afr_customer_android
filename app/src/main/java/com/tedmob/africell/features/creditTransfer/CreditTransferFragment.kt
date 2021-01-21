package com.tedmob.africell.features.creditTransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import com.tedmob.africell.util.setText
import com.tedmob.africell.util.validation.PhoneNumberHelper
import kotlinx.android.synthetic.main.fragment_credit_transfer.*
import javax.inject.Inject


class CreditTransferFragment : BaseFragment(), Liv.Action {
    private var liv: Liv? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<CreditTransferViewModel> { viewModelFactory }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_credit_transfer, R.layout.toolbar_default, false)
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.credit_transfer)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liv = initLiv()
        liv?.start()
        sendBtn.setOnClickListener { liv?.submitWhenValid() }
        bindData()
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()

        return Liv.Builder()
            .add(mobileNumberLayout, notEmptyRule)
            .add(amountLayout, notEmptyRule)
            .submitAction(this)
            .build()

    }

    private fun bindData() {
     /*   observeResourceInline(viewModel.cardsData) {
            recyclerView.adapter = adapter
            adapter.setItems(it)
        }
*/
        observeResource(viewModel.creditTransferData) {
            showMessageDialog(it.resultText.orEmpty(), getString(R.string.close)) {
             /*   liv?.dispose()
                amountLayout.setText("")
                mobileNumberLayout.setText("")
                liv?.start()*/
                findNavController().popBackStack()
            }
        }
    }


    override fun performAction() {
        val formatted = PhoneNumberHelper.getFormattedIfValid("", mobileNumberLayout.getText())?.replace("+", "")
        formatted?.let {
            viewModel.creditTransfer(it, amountLayout.getText())
        } ?: showMessage(getString(R.string.phone_number_not_valid))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        liv?.dispose()
    }
}

