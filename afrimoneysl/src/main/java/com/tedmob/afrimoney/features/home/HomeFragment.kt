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
import com.tedmob.afrimoney.databinding.DialogBalanceBinding
import com.tedmob.afrimoney.databinding.DialogPendingTransactionsBinding
import com.tedmob.afrimoney.databinding.DialogTransactionResultBinding
import com.tedmob.afrimoney.databinding.FragmentHomeNewBinding
import com.tedmob.afrimoney.databinding.QrcodeAlertBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.dialogs_utils.dialog.showVBDialog
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


        }


        observeResource(viewModel.balance) {
            it.let {


                binding?.userName?.text = it.userName

                binding?.balanceLayout?.setDebouncedOnClickListener { _ ->

                    showVBDialog(DialogBalanceBinding::inflate) {
                        isCancellable(true)
                        setCorner(40f)
                        buildDialog { bindDialog, dialog ->

                            bindDialog.normalWalletTv.text = getString(
                                R.string.normal_wallet_,
                                if ((it.normalWallet.split("."))[1] == "00") {
                                    it.normalWallet.toDoubleOrNull()?.toInt().toString() + "GMD"
                                } else it.normalWallet + "GMD"
                            )

                            bindDialog.bonusWalletTv.text = getString(
                                R.string.bonus_wallet_,
                                if ((it.bonusWallet.split("."))[1] == "00") {
                                    it.bonusWallet.toDoubleOrNull()?.toInt().toString() + "GMD"
                                } else it.bonusWallet + "GMD"
                            )

                            bindDialog.remittanceTv.text = getString(
                                R.string.remittance_wallet_,
                                if ((it.remittanceWallet.split("."))[1] == "00") {
                                    it.remittanceWallet.toDoubleOrNull()?.toInt().toString() + "GMD"
                                } else it.remittanceWallet + "GMD"
                            )

                            bindDialog.closeBtn.setDebouncedOnClickListener {
                                dialog.dismiss()
                            }


                        }
                    }


                }
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
            findNavController().navigate(R.id.nav_pending_transactions)
            //dialogPendingTransactions {}
        }
        withdrawMoneyOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_withdraw)
        }
        buyAirtimeOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_airtime)
        }
        buyBundlesOption.setDebouncedOnClickListener {
            findNavController().navigate(R.id.nav_services)
        }
        payBillsOption.setDebouncedOnClickListener {
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