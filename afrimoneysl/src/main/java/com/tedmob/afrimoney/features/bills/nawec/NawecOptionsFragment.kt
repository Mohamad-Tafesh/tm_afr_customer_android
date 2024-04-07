package com.tedmob.afrimoney.features.bills.nawec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.app.BaseVBBottomSheetFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentNawecOptionsBinding

import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NawecOptionsFragment : BaseVBBottomSheetFragment<FragmentNawecOptionsBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentNawecOptionsBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            closeButton.setDebouncedOnClickListener { findNavController().popBackStack() }

            buyNawec.setDebouncedOnClickListener {
                findNavController().navigate(NawecOptionsFragmentDirections.actionNawecOptionsFragmentToBuyNawecFragment())
            }

            addMeterNumber.setDebouncedOnClickListener {
                findNavController().navigate(NawecOptionsFragmentDirections.actionNawecOptionsFragmentToNawecAddFragment())
            }
            deleteMeterNumber.setDebouncedOnClickListener {
                findNavController().navigate(NawecOptionsFragmentDirections.actionNawecOptionsFragmentToNawecDeleteFragment())
            }
        }

    }


}