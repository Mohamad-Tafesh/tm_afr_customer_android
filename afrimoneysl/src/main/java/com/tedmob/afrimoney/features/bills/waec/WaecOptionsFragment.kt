package com.tedmob.afrimoney.features.bills.waec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.app.BaseVBBottomSheetFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentWaecOptionsBinding

import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaecOptionsFragment : BaseVBBottomSheetFragment<FragmentWaecOptionsBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentWaecOptionsBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            closeButton.setDebouncedOnClickListener { findNavController().popBackStack() }

            checkResultToken.setDebouncedOnClickListener {
              findNavController().navigate(WaecOptionsFragmentDirections.actionWaecOptionsFragmentToWaecConfirmationFragment("2"))

            }
            eRegistrationToken.setDebouncedOnClickListener {
                findNavController().navigate(WaecOptionsFragmentDirections.actionWaecOptionsFragmentToWaecConfirmationFragment("1"))

            }
        }

    }


}