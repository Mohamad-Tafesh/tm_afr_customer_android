package com.africell.africell.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentChangePasswordBinding
import com.africell.africell.databinding.ToolbarImageBinding
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideActivityViewModel
import com.africell.africell.util.getText
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.ConfirmPasswordRule
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseVBFragment<FragmentChangePasswordBinding>(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideActivityViewModel<ChangePasswordViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentChangePasswordBinding::inflate, false, ToolbarImageBinding::inflate)
    }

    override fun configureToolbar() {
        getToolbarBindingAs<ToolbarImageBinding>()?.run {
            actionbar?.show()
            toolbarImage.setActualImageResource(R.mipmap.main_top3)
            actionbar?.setDisplayHomeAsUpEnabled(true)
            actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
            toolbarTitle?.text = getString(R.string.change_password)
            actionbar?.title = ""
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        liv = initLiv()
        liv?.start()
        bindData()
        /*saveData()
        retrieveData()
        */
        withVBAvailable {
            registerButton.setOnClickListener {
                liv?.submitWhenValid()
            }
        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val emailRule = EmailRule(getString(R.string.invalid_email))
        val passwordRule =
            ConfirmPasswordRule(requireBinding().newPasswordLayout, requireBinding().confirmPasswordLayout)
        return Liv.Builder()
            .add(requireBinding().oldPasswordLayout, notEmptyRule)
            .add(requireBinding().newPasswordLayout, notEmptyRule)
            .add(requireBinding().confirmPasswordLayout, passwordRule)
            .submitAction(this)
            .build()
    }

    private fun bindData() {
        observeResource(viewModel.changePasswordData, {
            showMaterialMessageDialog(getString(R.string.successful), it.resultText.orEmpty()) {
                findNavController().popBackStack()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        liv?.dispose()
    }

    override fun performAction() {
        withVBAvailable {
            viewModel.changePassword(
                oldPasswordLayout.getText(),
                newPasswordLayout.getText(),
                confirmPasswordLayout.getText()
            )
        }
    }


}
