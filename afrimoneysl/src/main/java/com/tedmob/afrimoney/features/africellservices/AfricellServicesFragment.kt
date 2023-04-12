package com.tedmob.afrimoney.features.africellservices


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import coil.load
import com.tedmob.afrimoney.R

import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.BundlelistParent

import com.tedmob.afrimoney.databinding.*
import com.tedmob.afrimoney.features.africellservices.databundle.DataBundleFragmentArgs
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AfricellServicesFragment : BaseVBFragment<FragmentAfricellServicesBinding>() {

    private val viewModel by provideViewModel<AfricellServicesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentAfricellServicesBinding::inflate, true)
    }


    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getServices()

        withVBAvailable {

            observeResourceInline(viewModel.data) {

                setupServices(it)
            }

            airtime.setOnClickListener {
                findNavController().navigate(AfricellServicesFragmentDirections.actionAfricellServicesFragmentToNavAirtime())

            }

        }


    }

    private fun FragmentAfricellServicesBinding.setupServices(list: List<BundlelistParent>) {
        servicesRV.adapter = adapter(list) {
            viewBinding(ItemAfricellServicesBinding::inflate)
            onBindItemToViewBinding<ItemAfricellServicesBinding> {

                img.load(it.icon) {
                    error(R.drawable.service_place_holder)
                    fallback(R.drawable.service_place_holder)

                }
                title.setText(it.displayName)


                root.setOnClickListener { view ->
                    findNavController().navigate(
                        R.id.nav_databundle,
                        DataBundleFragmentArgs(it).toBundle()
                    )
                }

            }
        }
    }


}