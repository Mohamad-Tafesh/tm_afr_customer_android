package com.tedmob.afrimoney.features.pendingtransactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.data.api.dto.PendingTransactionsItemDTO
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingTransactionsFragment : BaseVBFragment<FragmentTransactionsBinding>() {

    private val viewModel by provideViewModel<PendingTransactionsViewModel>()

    private val args by navArgs<PendingTransactionsFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(
            container,
            FragmentTransactionsBinding::inflate,
            true,
            ToolbarCustomBinding::inflate
        )
    }



    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.hide()

        if (args.service.equals("CASHOUT")) toolbar?.title = "CashOut" else toolbar?.title =
            "Merchant Payment"
        toolbar?.setNavigationIcon(R.drawable.ic_nav_back_black)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.pending_transactionsMainFragment, false)
        }

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupRecyclerView()
        //bindData()
        observeResourceInline(viewModel.transactionData){ data ->
            setupTransactions(data)
            requireBinding().contentLL.showContent()
        }

        viewModel.getData(session.msisdn, args.pin, args.service)
    }




    private fun PendingTransactionsFragment.setupTransactions(transactions: List<PendingTransactionsItemDTO>) {
        requireBinding().transactionsRV.adapter = adapter(transactions) {
            viewBinding(ItemTransactionBinding::inflate)
            onBindItemToViewBinding<ItemTransactionBinding> {
                transactionid.text= it.transaction_id
                dateText.text= it.date
                amount.text= context?.getString(R.string.amount_currency,it.amount)
                initiated.text=it.from

                root.setOnClickListener(){ abc->
                    lateinit var type:String
                    if (args.service.equals("CASHOUT")) type="c" else type="m"
                            findNavController().navigate(   PendingTransactionsFragmentDirections.actionPendingtransactionsToConfirmPendingTransactionsFragment(it,type)
                    )
                }
            }

        }

    }

}

