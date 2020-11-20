package com.tedmob.africell.features.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.benitobertoli.liv.Liv
import com.benitobertoli.liv.rule.EmailRule
import com.benitobertoli.liv.rule.NotEmptyRule
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.provideActivityViewModel

import kotlinx.android.synthetic.main.fragment_set_password.*
import javax.inject.Inject

class SetPasswordFragment : BaseFragment(), Liv.Action {

    private var liv: Liv? = null
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideActivityViewModel<RegisterViewModel> { viewModelFactory }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_set_password, R.layout.toolbar_image, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
        //headerImage.setActualImageResource(R.mipmap.sign_up_top)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        //actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.title = getString(R.string.forgot_password)


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

    /*private fun saveData() {

        passwordLayout.saveText { viewModel.password = it }
        confirmPasswordLayout.saveText { viewModel.confirmPassword = it }
    }

    private fun retrieveData() {
        viewModel.email?.let { emailLayout.setText(it) }
        viewModel.password?.let { passwordLayout.setText(it) }
        viewModel.confirmPassword?.let { confirmPasswordLayout.setText(it) }
    }
*/
    private fun initLiv(): Liv {
        val notEmptyRule = NotEmptyRule()
        val emailRule = EmailRule(getString(R.string.invalid_email))
        //val passwordRule = ConfirmPasswordRule(passwordLayout, confirmPasswordLayout)
        return Liv.Builder()

            .add(passwordLayout, notEmptyRule)
           // .add(confirmPasswordLayout, passwordRule)
            .submitAction(this)
            .build()
    }

    private fun bindData() {
        observeResource(viewModel.updatedProfileData, {
            findNavController().navigate(R.id.action_setPasswordFragment_to_mainActivity)
            activity?.finish()
      /*      val directions = SetPasswordFragmentDirections.actionSetPasswordFragmentToVerificationFragment(email = emailLayout.getText(), password = passwordLayout.getText())
            findNavController().navigate(directions)
      */  })
    }


    override fun onDestroy() {
        super.onDestroy()
        liv?.dispose()
    }

    override fun performAction() {
        viewModel.setProfile()
    }


}
