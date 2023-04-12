package com.tedmob.afrimoney.features.bills.dstv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.app.BaseVBBottomSheetFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentDstvOptionsBinding

import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DstvOptionsFragment : BaseVBBottomSheetFragment<FragmentDstvOptionsBinding>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentDstvOptionsBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {
            closeButton.setDebouncedOnClickListener {findNavController().popBackStack() }

            checkDstv.setDebouncedOnClickListener {
                findNavController().navigate(DstvOptionsFragmentDirections.actionPayMyBillsFragmentToCheck())

            }
          renewDstv.setDebouncedOnClickListener {
                findNavController().navigate(DstvOptionsFragmentDirections.actionPayMyBillsFragmentToPayMyBillsDSTVNavigaion())
            }
        }

    }


}