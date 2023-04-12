package com.tedmob.afrimoney.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import coil.loadAny
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.entity.UserHomeData
import com.tedmob.afrimoney.databinding.FragmentHomeBinding
import com.tedmob.afrimoney.databinding.ItemHomeOptionBinding
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter
import com.tedmob.afrimoney.util.security.StringEncryptor
import com.tedmob.afrimoney.util.security.decryptWith
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {

    @Inject
    @Named("local-string")
    lateinit var encryptor: StringEncryptor

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<HomeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentHomeBinding::inflate, true)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.setBackgroundDrawable(resources.getDrawable(R.color.transparent))
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeResourceInline(viewModel.data) {
            setupUser(it)
            setupOptions(it.nbOfPendingTransaction)
        }

        viewModel.balance.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                it.data.let {


                    val newBalance =
                        (it.balance.toDouble() - it.fbr.toDouble() - it.fic.toDouble()).toString()


                    if ((newBalance.split("."))[1] == "00") {
                        val bal = newBalance.toDoubleOrNull()?.toInt()
                        binding?.balanceText?.text = bal.toString()+"NLe"
                    } else binding?.balanceText?.text = newBalance+"NLe"


                    binding?.userName?.text = it.userName
                }
            }
        }


        requireBinding().messagesLayout.setOnClickListener {
            //findNavController().navigate(R.id.nav_notif_messages)
        }

        requireBinding().transactionsLayout.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.fromHomeFragmentToEnterPin())
        }



        viewModel.getData()
        viewModel.getBalance()
    }

    private inline fun setupOptions(nb: Int) {
        withVBAvailable {
            optionsRV.adapter = adapter(getOptions().toMutableList()) {
                viewBinding(ItemHomeOptionBinding::inflate)
                onBindItemToViewBinding<ItemHomeOptionBinding> { option ->
                    image.loadAny(option.imageData)
                    label.setText(option.label)
                    if (option == HomeOption.PendingTransactions) {
                        //messagesBadge.isVisible=true
                        //messagesBadge.text=nb.toString()
                    } else {
                        //messagesBadge.isVisible=false

                    }
                    root.setDebouncedOnClickListener { redirectTo(option) }
                }
            }
        }
    }

    private inline fun getOptions() = listOf(
        HomeOption.TransferMoney,
        HomeOption.PendingTransactions,
        HomeOption.WithdrawMoney,
        HomeOption.BuyAirtime,
        HomeOption.PayMyBills,
        HomeOption.MerchantPayment,
        HomeOption.BankingServices,
        HomeOption.AfricellServices)


    private fun setupUser(data: UserHomeData) {
        withVBAvailable {
            /*data.messagesCount.let {
                messagesBadge.isVisible = it > 0
                messagesBadge.text = it.toString()
            }
            data.transactionsCount.let {
                transactionsBadge.isVisible = it > 0
                transactionsBadge.text = it.toString()
            }*/
        }
    }


    private fun redirectTo(option: HomeOption) {
        findNavController().navigate(option.destination, option.arguments)
    }
}