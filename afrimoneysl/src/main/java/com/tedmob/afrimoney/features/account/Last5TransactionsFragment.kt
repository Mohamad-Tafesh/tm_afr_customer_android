package com.tedmob.afrimoney.features.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.text.scale
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.LastTransaction
import com.tedmob.afrimoney.databinding.FragmentLast5TransactionsBinding
import com.tedmob.afrimoney.databinding.ItemLastTransactionBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class Last5TransactionsFragment : BaseVBFragment<FragmentLast5TransactionsBinding>() {

    private val viewModel by provideViewModel<Last5TransactionsViewModel>()
    private val args by navArgs<Last5TransactionsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentLast5TransactionsBinding::inflate, true)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            backButton.setDebouncedOnClickListener { findNavController().popBackStack() }

            transactionsRV.addItemDecoration(
                MaterialDividerItemDecoration(
                    transactionsRV.context,
                    MaterialDividerItemDecoration.VERTICAL
                )
                    .apply {
                        setDividerColorResource(transactionsRV.context, R.color.greyHighlight)
                        setDividerInsetStartResource(
                            transactionsRV.context,
                            R.dimen.divider_inset_start
                        )
                    }
            )
        }

        bindData()

        viewModel.getLastTransactions(args.pin)
    }

    private fun bindData() {
        observeResourceInline(viewModel.transactions) {
            withVBAvailable {
                setupTransactions(it)
            }
        }
    }


    private val serverDateFormat by lazy {
        SimpleDateFormat(
            LastTransaction.SERVER_DATE_FORMAT,
            Locale.ENGLISH
        )
    }
    private val localDateFormat by lazy { SimpleDateFormat("EEE dd MMM yyyy HH:mm", Locale.ENGLISH) }

    private fun FragmentLast5TransactionsBinding.setupTransactions(transactions: List<LastTransaction>) {
        transactionsRV.adapter = adapter(transactions) {
            viewBinding(ItemLastTransactionBinding::inflate)
            onBindItemToViewBinding<ItemLastTransactionBinding> {
                dateText.text = try {
                    it.txnDt
                        ?.let { serverDateFormat.parse(it) }
                        ?.let { localDateFormat.format(it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                totalText.text = buildSpannedString {
                    scale(1f) {
                        color(ContextCompat.getColor(requireContext(), R.color.black)) {

                            append(getString(R.string.total__format))
                        }
                    }

                    append(" " + getString(R.string.amount_currency, it.txnAmt.orEmpty()))
                }

                idText.text = buildSpannedString {
                    scale(1f) {
                        color(ContextCompat.getColor(requireContext(), R.color.black)) {

                            append(getString(R.string.id))
                        }
                    }

                    append(" " + it.txnId.orEmpty())
                }

                if (it.attr1Value == "MERCHPAY") {
                    merchantText.isVisible = true
                    merchantText.text = buildSpannedString {
                        scale(1f) {
                            color(ContextCompat.getColor(requireContext(), R.color.black)) {

                                append(getString(R.string.merchant_name))
                            }
                        }

                        append(" " + it.firstName.orEmpty() + " " + it.lastName.orEmpty())
                    }
                }

                typeText.text = buildSpannedString {
                    scale(1f) {
                        color(ContextCompat.getColor(requireContext(), R.color.black)) {

                            append(getString(R.string.type))
                        }
                    }

                    append(" " + it.service.orEmpty())
                }

                statusText.text = buildSpannedString {
                    scale(1f) {
                        color(ContextCompat.getColor(requireContext(), R.color.black)) {

                            append(getString(R.string.status))
                        }
                    }

                    append(
                        " " + if (it.txnStatus.orEmpty().equals("ts", true)) getString(R.string.transaction_successful)
                        else if (it.txnStatus.orEmpty().equals("tf", true)) getString(R.string.transaction_failed)
                        else it.txnStatus.orEmpty()
                    )
                }

                //totalText.text = getString(R.string.total__format, it.txnAmt.orEmpty())
            }
        }
    }
}