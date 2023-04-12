package com.tedmob.afrimoney.features.referfriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentReferFriendBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.hideKeyboard
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.getText
import com.tedmob.libraries.validators.FormValidator
import com.tedmob.libraries.validators.formValidator
import com.tedmob.libraries.validators.rules.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferFriendFragment : BaseVBFragment<FragmentReferFriendBinding>() {

    private val viewModel by provideViewModel<ReferFriendViewModel>()
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
        return createViewBinding(container, FragmentReferFriendBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()

        viewModel.getData()
        requireBinding().setupFormValidation()
    }

    private fun FragmentReferFriendBinding.setupFormValidation() {
        validator = formValidator {
            val notEmptyTextRule = NotEmptyRule(getString(R.string.mandatory_field))
            tilMobileNum.validate(notEmptyTextRule)
            onValid = { send() }
        }
    }

    private fun send() {
        withVBAvailable {
            Toast.makeText(
                requireContext(),
                tilMobileNum.getText(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun bindData() {
        observeResourceInline(viewModel.content) {
            withVBAvailable {

                getMobileNumber()

                btnRefer.setDebouncedOnClickListener {
                    hideKeyboard()
                    validator?.submit(viewLifecycleOwner.lifecycleScope)
                }
            }
        }
    }

    private fun FragmentReferFriendBinding.getMobileNumber(): String {
        return tilMobileNum.getText()
    }
}