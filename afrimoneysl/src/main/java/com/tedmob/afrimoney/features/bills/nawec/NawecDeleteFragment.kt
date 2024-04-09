package com.tedmob.afrimoney.features.bills.nawec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.api.dto.ClientDetails
import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NawecDeleteFragment :
    BaseVBFragmentWithImportContact<FragmentNawecDeleteBinding>() {

    private val viewModel by provideNavGraphViewModel<NawecViewModel>(R.id.nav_nawec)
    var meterList = mutableListOf<ClientDetails>()
    var client: NawecMeterData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentNawecDeleteBinding::inflate, true)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.clientId?.setOnClickListener {
            findNavController().navigate(
                NawecDeleteFragmentDirections.actionNawecDeleteFragmentToNawecMetersBottomSheetFragment(
                    meterList.toTypedArray()
                )
            )
        }


        observeResourceInline(viewModel.clients) {
            meterList = it.list.toMutableList()
        }

        observeResource(viewModel.data) {
            binding?.clientId?.text = it.meterNumber
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<NawecMeterData>("data")
            ?.observe(
                viewLifecycleOwner
            ) { data ->
                binding?.clientId?.text = data.meterNumber
                client = NawecMeterData(data.meterNumber, data.nickname)

            }

        observeResourceFromButton(viewModel.proceedToConfirmDelete, R.id.proceedButton) {
            proceed()
        }



        withVBAvailable {


            proceedButton.setDebouncedOnClickListener {
                if (client != null) {
                    viewModel.proceedDelete(client?.meterNumber.orEmpty())
                } else {
                    showMessage(resources.getString(R.string.please_select_your_meter_number_first))
                }
            }

            viewModel.getClients()
        }


    }


    private fun proceed() {
        findNavController().navigate(NawecDeleteFragmentDirections.actionNawecDeleteFragmentToNawecDeleteConfirmationFragment())
    }


}