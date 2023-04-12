package com.tedmob.afrimoney.features.banking

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.data.entity.Bank
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.AdapterHolderVB
import com.tedmob.afrimoney.util.adapter.adapter
import com.tedmob.afrimoney.util.navigation.runIfFrom
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BankingServicesFragment : BaseVBFragment<FragmentBankingServicesBinding>() {

    private val viewModel by provideViewModel<BankingServicesViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentBankingServicesBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeResourceInline(viewModel.banks, requireBinding().contentLL) {
            setupBanks(it)
        }

        viewModel.getBanks()
    }

    private fun setupBanks(banks: List<Bank>) {
        val (linked, notLinked) = banks.partition { it.isLinked }
        val newList = linked + notLinked

        binding?.banksRV?.adapter = adapter<Bank> {
            getItemCount = {
                newList.size + (if (linked.isNotEmpty()) 1 else 0) + (if (notLinked.isNotEmpty()) 1 else 0)
            }

            provideHolders {
                val linkedHeaderPosition = 0
                val notLinkedHeaderPosition =
                    (if (linked.isNotEmpty()) (linkedHeaderPosition + 1) else 0) + linked.size

                if (linked.isNotEmpty()) {
                    viewBindingIf(
                        { it == linkedHeaderPosition },
                        ItemBankHeaderBinding::inflate
                    ) {
                        label.setText(R.string.linked_banks)
                        label.setTextColor(root.context.resolveAttribute(R.attr.colorPrimary).data)
                    }

                    viewBindingIf(
                        { it in (linkedHeaderPosition + 1) until notLinkedHeaderPosition },
                        ItemBankLinkedBinding::inflate
                    )
                }

                if (notLinked.isNotEmpty()) {
                    viewBindingIf(
                        { it == notLinkedHeaderPosition },
                        ItemBankHeaderBinding::inflate
                    ) {
                        label.setText(R.string.available_banks)
                        label.setTextColor(root.context.resolveAttribute(android.R.attr.textColorPrimary).data)
                    }

                    viewBindingIf(
                        { it > notLinkedHeaderPosition },
                        ItemBankNotLinkedBinding::inflate
                    )
                }
            }

            onBindHolder = { holder, position ->
                if (linked.isNotEmpty()) {
                    when (getItemViewType(position)) {
                        1 -> {
                        }
                        2 -> {
                            (holder as AdapterHolderVB<ItemBankLinkedBinding>).viewBinding.run {
                                val item = newList.getLinkedFromPosition(position)
                                nameText.text = item.bname
                                root.setDebouncedOnClickListener { onBankClicked(item) }
                            }
                        }
                        3 -> {
                        }
                        4 -> {
                            (holder as AdapterHolderVB<ItemBankNotLinkedBinding>).viewBinding.run {
                                val item = newList.getNonLinkedFromPosition(position)
                                nameText.text = item.bname
                                root.setDebouncedOnClickListener { showDialog(item) }
                            }
                        }
                        else -> throw IllegalArgumentException("Incorrect implementation")
                    }
                } else {
                    when (getItemViewType(position)) {
                        1 -> {
                        }
                        2 -> {
                            (holder as AdapterHolderVB<ItemBankNotLinkedBinding>).viewBinding.run {
                                val item = newList.getNonLinkedFromPosition(position)
                                nameText.text = item.bname
                                root.setDebouncedOnClickListener { showDialog(item) }
                            }
                        }
                        else -> throw IllegalArgumentException("Incorrect implementation")
                    }
                }
            }
        }
    }

    private fun onBankClicked(bank: Bank) {
        findNavController().runIfFrom(R.id.bankingServicesFragment) {
            navigate(
                BankingServicesFragmentDirections.actionBankingServicesFragmentToChooseBankingServiceTypeFragment(
                    bank
                )
            )
        }
    }


    @SuppressLint("SetTextI18n")
    fun showDialog(
        bank: Bank,
        onDismiss: (() -> Unit)? = null
    ) {
        this.let {
            val viewBinding = DialogBankNotLinkedBinding.inflate(
                it.layoutInflater,
                FrameLayout(requireContext()),
                false
            )

            viewBinding.run {

                resultTitle.setText(bank.bname)
                resultMessage.setText("Visit the bank's branch to link your bank account with AfriMoney")

            }

            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(viewBinding.root)
                .setOnDismissListener { onDismiss?.invoke() }
                .show()


            viewBinding.run {
                closeButton.setOnClickListener {
                    dialog.dismiss()
                    onDismiss?.invoke()
                }
            }
        }
    }


    private inline fun Context.resolveAttribute(@AttrRes attributeRes: Int): TypedValue =
        TypedValue().apply {
            theme.resolveAttribute(attributeRes, this, true)
        }

    private inline fun List<Bank>.getLinkedFromPosition(position: Int): Bank {
        //assume this means we always have a linked bank
        return get(position - 1)
    }

    private fun List<Bank>.getNonLinkedFromPosition(position: Int): Bank {
        //assume this means we always have a not linked bank
        val linked = filter { it.isLinked }
        val index = if (linked.isNotEmpty()) {
            position - 1 - 1
        } else {
            position - 1
        }
        return get(index)
    }

}