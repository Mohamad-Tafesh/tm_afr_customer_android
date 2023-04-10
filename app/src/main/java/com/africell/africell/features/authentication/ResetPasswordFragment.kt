package com.africell.africell.features.authentication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.ApiContract
import com.africell.africell.databinding.FragmentSetPasswordBinding
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
class ResetPasswordFragment : BaseVBFragment<FragmentSetPasswordBinding>(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideActivityViewModel<ResetPasswordViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentSetPasswordBinding::inflate, false, ToolbarImageBinding::inflate)
    }

    override fun configureToolbar() {
        getToolbarBindingAs<ToolbarImageBinding>()?.run {
            actionbar?.show()
            toolbarImage.setActualImageResource(R.mipmap.main_top3)
            actionbar?.setDisplayHomeAsUpEnabled(true)
            actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
            toolbarTitle?.text = getString(R.string.forgot_password)
            actionbar?.title = ""
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getToolbarBindingAs<ToolbarImageBinding>()?.run {
            setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.FORGOT_PASSWORD)
        }
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
        val passwordRule = ConfirmPasswordRule(requireBinding().passwordLayout, requireBinding().confirmPasswordLayout)
        return Liv.Builder()

            .add(requireBinding().passwordLayout, notEmptyRule)
            .add(requireBinding().confirmPasswordLayout, passwordRule)
            .submitAction(this)
            .build()
    }

    private fun bindData() {
        observeResource(viewModel.resetData, {
            findNavController().navigate(R.id.action_setPasswordFragment_to_mainActivity)
            activity?.finish()
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        liv?.dispose()
    }

    override fun performAction() {
        withVBAvailable {
            viewModel.resetPassword(passwordLayout.getText(), confirmPasswordLayout.getText())
        }
    }


}
