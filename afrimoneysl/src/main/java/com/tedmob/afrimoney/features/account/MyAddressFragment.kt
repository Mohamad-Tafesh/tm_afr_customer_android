package com.tedmob.afrimoney.features.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.ComboEntry
import com.tedmob.afrimoney.data.entity.MyAddressFields
import com.tedmob.afrimoney.data.entity.RegionItem
import com.tedmob.afrimoney.databinding.FragmentMyAddressBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.setText
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAddressFragment : BaseVBFragment<FragmentMyAddressBinding>() {

    companion object {
        const val KEY__RESULT = "MyAddressFragment__result"
    }

    class Result(
        val coordinates: MapLatLng,
        val selectedProvidence: String,
        val selectedCity: String,
        val selectedStreet: String,
    )


    private val viewModel by provideNavGraphViewModel<MyAddressViewModel>(R.id.nav_address)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentMyAddressBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.saveButton?.isEnabled = false

        bindData()
        bindResult()

        withVBAvailable {
            cancelButton.setDebouncedOnClickListener {
                hideKeyboard()
                findNavController().popBackStack()
            }

            locationFromMap.setDebouncedOnClickListener {
                hideKeyboard()
                findNavController().navigate(
                    MyAddressFragmentDirections.actionMyAddressFragmentToSelectAddressFromMapFragment(
                        viewModel.coordinates
                    )
                )
            }

            providenceInput.adapter = ArrayAdapter(
                providenceInput.context,
                R.layout.support_simple_spinner_dropdown_item,
                resources.let {
                    it.getStringArray(R.array.region)
                        .zip(it.getStringArray(R.array.region_values)) { t1, t2 ->
                            RegionItem(t1, t2)
                        }
                }
            )
        }

        binding?.contentLL?.showContent()
    }

    private fun bindData() {

        binding?.setupData()

    }

    private fun FragmentMyAddressBinding.setupData() {

        val validator = formValidator {
            val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

            providenceInput.validate(notEmptyRule)
            cityInput.validate(notEmptyRule)
            streetInput.validate(notEmptyRule)

            onValid = { saveResult() }
        }
        saveButton.isEnabled = true
        saveButton.setDebouncedOnClickListener {
            hideKeyboard()
            validator.submit(viewLifecycleOwner.lifecycleScope)
        }
    }


    private fun bindResult() {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<SelectAddressFromMapFragment.MapResult>(SelectAddressFromMapFragment.LIVE_DATA__MAP_RESULT)
            ?.observe(viewLifecycleOwner) {
                when (it) {
                    SelectAddressFromMapFragment.MapResult.Cancelled -> {}
                    is SelectAddressFromMapFragment.MapResult.Success -> {
                        viewModel.coordinates = it.location
                        val address = it.address
                        withVBAvailable {
                            streetInput.setText(address?.thoroughfare.orEmpty())
                        }
                    }
                }
            }
    }


    private fun saveResult() {
        withVBAvailable {
            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<Result>(KEY__RESULT)
                ?.value = Result(
                viewModel.coordinates ?: MapLatLng(0.0, 0.0),
                providenceInput.getText(),
                cityInput.getText(),
                streetInput.getText(),
            )
            findNavController().popBackStack()
        }
    }
}