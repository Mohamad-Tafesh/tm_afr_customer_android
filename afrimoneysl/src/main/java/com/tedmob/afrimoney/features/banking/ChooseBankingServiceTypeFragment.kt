package com.tedmob.afrimoney.features.banking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.app.BaseVBBottomSheetFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentChooseBankingServiceTypeBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseBankingServiceTypeFragment : BaseVBBottomSheetFragment<FragmentChooseBankingServiceTypeBinding>() {

    private val args by navArgs<ChooseBankingServiceTypeFragmentArgs>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentChooseBankingServiceTypeBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            closeButton.setDebouncedOnClickListener { findNavController().popBackStack() }

            bankToWallet.setDebouncedOnClickListener {
                findNavController().navigate(
                    ChooseBankingServiceTypeFragmentDirections
                        .actionChooseBankingServiceTypeFragmentToBankToWalletFragment(args.bank)
                )
            }
            walletToBank.setDebouncedOnClickListener {
                findNavController().navigate(
                    ChooseBankingServiceTypeFragmentDirections
                        .actionChooseBankingServiceTypeFragmentToWalletToBankFragment(args.bank)
                )
            }
            balanceEnquiry.setDebouncedOnClickListener {
                findNavController().navigate(
                    ChooseBankingServiceTypeFragmentDirections
                        .actionChooseBankingServiceTypeFragmentToBalanceEnquiryFragment(args.bank)
                )
            }
        }
    }
}