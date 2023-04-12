package com.tedmob.afrimoney.features.bills.dstv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.IdTypeItem
import com.tedmob.afrimoney.databinding.FragmentRenewDstvBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.spinner.MaterialSpinner
import com.tedmob.afrimoney.ui.spinner.OnItemSelectedListener
import com.tedmob.afrimoney.ui.viewmodel.observeResource
import com.tedmob.afrimoney.ui.viewmodel.observeResourceFromButton
import com.tedmob.afrimoney.ui.viewmodel.provideNavGraphViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.phone.BaseVBFragmentWithImportContact
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class RenewDSTVFragment : BaseVBFragmentWithImportContact<FragmentRenewDstvBinding>() {

    private val viewModel by provideNavGraphViewModel<RenewDSTVViewModel>(R.id.nav_dstv)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentRenewDstvBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getPress()

        observeResourceFromButton(viewModel.proceedToConfirm,R.id.proceedButton) {
            proceed()

        }

        observeResource(viewModel.data) {
            withVBAvailable {
                val list1: List<String> = buildList<String> {
                    for (index in 0 until it.size) {
                        add(it.get(index).name)
                    }
                }
                language.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    resources.let { list1 }
                )




                language.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: MaterialSpinner,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        subsType.isEnabled = true
                        val list2: List<String> = buildList<String> {
                            for (index2 in 0 until it.get(position).SubscriptionType.size) {
                                add(it.get(position).SubscriptionType.get(index2))
                            }
                        }
                        subsType.adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.support_simple_spinner_dropdown_item,
                            resources.let { list2 }
                        )
                    }

                    override fun onNothingSelected(parent: MaterialSpinner) {
                    }
                }

            }
        }


        withVBAvailable {


            val validator = setupValidation()
            image.load(R.drawable.paymybillsdstv) {
                transformations(CircleCropTransformation())
            }
            nbOfMonths.adapter = ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                resources.let {
                    it.getStringArray(R.array.nb_of_Months)
                        .zip(it.getStringArray(R.array.nb_of_Months)) { t1, t2 ->
                            IdTypeItem(t1, t2)
                        }
                }
            )

            proceedButton.setDebouncedOnClickListener { validator.submit(viewLifecycleOwner.lifecycleScope) }
        }


    }

    private fun FragmentRenewDstvBinding.setupValidation(): FormValidator = formValidator {
        val notEmptyRule = NotEmptyRule(getString(R.string.mandatory_field))

        smartCard.validate(
            notEmptyRule,
        )

        language.validate(
            notEmptyRule,
        )
        subsType.validate(
            notEmptyRule,
        )
        nbOfMonths.validate(
            notEmptyRule,
        )


        onValid = {
            viewModel.proceed(
                smartCard.getText(),
                language.getText(),
                subsType.getText(),
                nbOfMonths.getText()
            )

        }
    }


    private fun proceed() {

        findNavController().navigate(
            RenewDSTVFragmentDirections.actionPayMyBillsRenewDSTVFragmentToPayMyBillsConfirmRenewDSTVFragment()
        )
    }


}