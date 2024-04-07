package com.tedmob.afrimoney.features.bills.nawec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tedmob.afrimoney.app.BaseVBBottomSheetFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentNawecMeterBottomSheetBinding
import com.tedmob.afrimoney.databinding.ItemMeterBinding
import com.tedmob.afrimoney.util.adapter.adapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NawecMetersBottomSheetFragment :
    BaseVBBottomSheetFragment<FragmentNawecMeterBottomSheetBinding>() {

    val args by navArgs<NawecMetersBottomSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentNawecMeterBottomSheetBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withVBAvailable {

            binding?.rv?.adapter = adapter(args.data.toMutableList()) {
                viewBinding(ItemMeterBinding::inflate)
                onBindItemToViewBinding<ItemMeterBinding> {
                    client.text = it.clientName.orEmpty() + "\n" + it.clientId.orEmpty()

                    root.setOnClickListener { v ->
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            "data", NawecMeterData(
                                it.clientId.orEmpty(),
                                it.clientName.orEmpty()
                            )
                        )
                        findNavController().popBackStack()
                    }

                }


            }

        }

    }


}