package com.tedmob.afrimoney.features.home

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.DialogPendingTransactionsBinding
import com.tedmob.afrimoney.databinding.DialogTransactionResultBinding
import com.tedmob.afrimoney.databinding.FragmentHomeNewBinding
import com.tedmob.afrimoney.databinding.QrcodeAlertBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.security.StringEncryptor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : BaseVBFragment<FragmentHomeNewBinding>() {

    @Inject
    @Named("local-string")
    lateinit var encryptor: StringEncryptor

    @Inject
    lateinit var appSessionNavigator: AppSessionNavigator

    private val viewModel by provideViewModel<HomeViewModel>()

    private var hide = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.addMenuProvider(provideMenu(), viewLifecycleOwner)
        return createViewBinding(container, FragmentHomeNewBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.setBackgroundDrawable(resources.getDrawable(R.color.transparent))
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withVBAvailable {
            setupToolbar()
            setupOptions()

            balanceLayout.setDebouncedOnClickListener {
                if (hide){
                    hide = false
                    balanceText.isVisible=true
                    moneyIcon.isVisible=false
                    balanceSubtitle.text=getString(R.string.hide_my_balance)
                }else{
                    hide = true
                    balanceText.isVisible=false
                    moneyIcon.isVisible=true
                    balanceSubtitle.text=getString(R.string.my_balance)
                }

            }



        }


        observeResource(viewModel.balance) {//TODO add observe inline
            it.let {


                val newBalance = it.balance
                //  (it.balance.toDouble() - it.fbr.toDouble() - it.fic.toDouble()).toString()


                if ((newBalance.split("."))[1] == "00") {
                    val bal = newBalance.toDoubleOrNull()?.toInt()
                    binding?.balanceText?.text = bal.toString() + "GMD"
                } else binding?.balanceText?.text = newBalance + "GMD"


                binding?.userName?.text = it.userName
            }
        }


        requireBinding().transactionsLayout.setOnClickListener {

            findNavController().navigate(HomeFragmentDirections.fromHomeFragmentToEnterPin())
        }




        viewModel.getData()
        viewModel.getBalance()

    }


    private fun FragmentHomeNewBinding.setupToolbar() {
        actionbar?.show()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun FragmentHomeNewBinding.setupOptions() {

        requireBinding().accountLayout.setOnClickListener {

            findNavController().navigate(R.id.accountFragment)
        }


        transactionsLayout.setDebouncedOnClickListener {
            //findNavController().navigate(R.id.)
        }
        transferMoneyOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_transfer_money)
        }
        pendingTransactionsOption.setDebouncedOnClickListener {
            // findNavController().navigate(R.id.nav_pending_transactions)
            dialogPendingTransactions {}
        }
        withdrawMoneyOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_withdraw)
        }
        africellServices.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_services)
        }
        payMyBillsOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_pay_my_bills)
        }
        merchantPaymentOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_merchant_payment)
        }
        bankingServicesOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_banking_services)
        }
    }

    fun showDialog(
        onDismiss: (() -> Unit)? = null
    ) {
        val viewBinding = QrcodeAlertBinding.inflate(layoutInflater, FrameLayout(requireContext()), false)

        viewBinding.run {
            val code = session.msisdn
            try {
                val requiredWidth =
                    (resources.displayMetrics.widthPixels * 0.75).roundToInt()

                val bitmap = BarcodeEncoder().encodeBitmap(
                    code, BarcodeFormat.QR_CODE,
                    requiredWidth, requiredWidth
                )
                barcodeImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
            .setView(viewBinding.root)

            .setOnDismissListener { onDismiss?.invoke() }
            .show()

    }

    fun provideMenu() = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_barcode, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.barcode -> {
                    showDialog()
                    true
                }

                else -> false

            }


        }
    }

    fun dialogPendingTransactions(
        onDismiss: (() -> Unit)? = null
    ) {
        activity?.let {
            val viewBinding = DialogPendingTransactionsBinding.inflate(it.layoutInflater, FrameLayout(it), false)

            val dialog = MaterialAlertDialogBuilder(it)
                .setView(viewBinding.root)
                .setOnDismissListener { onDismiss?.invoke() }
                .show()

            viewBinding.run {
                closeButton.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
    }

}