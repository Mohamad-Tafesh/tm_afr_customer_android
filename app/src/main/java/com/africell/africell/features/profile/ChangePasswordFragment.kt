package com.africell.africell.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.ConfirmPasswordRule
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.ui.viewmodel.observeResource
import com.africell.africell.ui.viewmodel.provideActivityViewModel
import com.africell.africell.util.getText
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.toolbar_image.*

class ChangePasswordFragment : BaseFragment(), Liv.Action {

    private var liv: Liv? = null


    private val viewModel by provideActivityViewModel<ChangePasswordViewModel> { viewModelFactory }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_change_password, R.layout.toolbar_image, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
        toolbarImage.setActualImageResource(R.mipmap.main_top3)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        toolbarTitle?.text = getString(R.string.change_password)
        actionbar?.title = ""

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        liv = initLiv()
        liv?.start()
        bindData()
        /*saveData()
        retrieveData()
        */
        registerButton.setOnClickListener {
            liv?.submitWhenValid()
        }
    }

    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val emailRule = EmailRule(getString(R.string.invalid_email))
        val passwordRule = ConfirmPasswordRule(newPasswordLayout, confirmPasswordLayout)
        return Liv.Builder()
            .add(oldPasswordLayout, notEmptyRule)
            .add(newPasswordLayout, notEmptyRule)
            .add(confirmPasswordLayout, passwordRule)
            .submitAction(this)
            .build()
    }

    private fun bindData() {
        observeResource(viewModel.changePasswordData, {
            showMaterialMessageDialog(getString(R.string.successful),it.resultText.orEmpty()) {
                findNavController().popBackStack()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        liv?.dispose()
    }

    override fun performAction() {
        viewModel.changePassword(oldPasswordLayout.getText(),newPasswordLayout.getText(), confirmPasswordLayout.getText())
    }


}
