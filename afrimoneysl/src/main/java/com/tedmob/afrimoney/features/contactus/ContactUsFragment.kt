package com.tedmob.afrimoney.features.contactus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentContactUsBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.intents.email
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactUsFragment : BaseVBFragment<FragmentContactUsBinding>() {

    private val viewModel by provideViewModel<ContactUsViewModel>()
    private var validator: FormValidator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun configureToolbar() {
        actionbar?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentContactUsBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()

        viewModel.getData()

        requireBinding().setupFormValidation()
    }

    private fun FragmentContactUsBinding.setupFormValidation() {
        validator = formValidator {
            val notEmptyTextRule = NotEmptyRule(getString(R.string.mandatory_field))

            tilSupType.validate(notEmptyTextRule)
            tilMessage.validate(notEmptyTextRule)

            onValid = { send() }
        }
    }

    private fun send() {
        withVBAvailable {
            email("support@afrimoney.sl", "", "Afrimoney Contact Support", tilMessage.getText())
        }
    }

    private fun bindData() {
        observeResourceInline(viewModel.content) {
            withVBAvailable {
                val supportTypes = listOf("One", "Two", "Three", "Four", "Five")
                val arrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    supportTypes
                )

                actvSupportType.setAdapter(arrayAdapter)

                btnSendMessage.setDebouncedOnClickListener {
                    hideKeyboard()
                    validator?.submit(viewLifecycleOwner.lifecycleScope)
                }
            }
        }
    }

}
