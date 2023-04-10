package com.africell.africell.features.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.Resource
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentSettingsBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.features.launch.RootActivity
import com.africell.africell.ui.viewmodel.observeNotNull
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.locale.LocaleHelper
import com.africell.africell.util.removeUserIdentification
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class SettingsFragment : BaseVBFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var session: SessionRepository

    @Inject
    lateinit var firebaseCrashlytics: FirebaseCrashlytics

    private val viewModel by provideViewModel<SettingsViewModel> { viewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentSettingsBinding::inflate, false, ToolbarDefaultBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        withVBAvailable {
            setupImageBanner(imageView, ApiContract.Params.BANNERS, ApiContract.ImagePageName.SETTINGS)
            bindOneSignalStateTo(notificationsLayout, notificationsSwitch)
            setupLanguage()
            bindAppVersionTo(appVersionLayout, appVersionSummaryText)
            setupLoginLogout()
            setUpDeleteAccount()
        }
    }

    private fun FragmentSettingsBinding.setupLanguage() {
        if (BuildConfig.MULTI_LANG) {
            languageLayout.visibility = View.VISIBLE
            bindLanguageMenuTo(
                languageLayout,
                languageTitleText,
                languageSummaryText,
                mode = LanguageMenuMode.DROPDOWN
            ) { popup, language, code ->
                popup?.dismiss()

                if (code != session.language) {
                    session.userChangedLanguage = true

                    //fixme Force applying preference change before system does in order to change app resources' configuration
                    session.language = code
                    changeAppConfiguration()

                    startRootActivity()
                }
            }
        } else {
            languageLayout.visibility = View.GONE
        }
    }

    private fun FragmentSettingsBinding.setupLoginLogout() {
        //loginLogoutTitleText.setText(if (session.isLoggedIn()) R.string.logout else R.string.login)
        loginLogoutLayout.visibility = if (session.isLoggedIn()) View.VISIBLE else View.GONE
        loginLogoutLayout.setOnClickListener {
            if (session.isLoggedIn()) {
                viewModel.logout()
            } else {
                session.invalidateSession()
                removeUserIdentification(firebaseAnalytics, firebaseCrashlytics)
                startRootActivity()
            }
        }
        bindData()
    }

    private fun FragmentSettingsBinding.setUpDeleteAccount() {
        deleteAccountLayout.visibility = if (session.isLoggedIn()) View.VISIBLE else View.GONE
        deleteAccountLayout.setOnClickListener {
            showMaterialDeleteMessageDialog(
                getString(R.string.warning),
                getString(R.string.are_you_sure_want_to_delete),
                getString(R.string.delete_account)
            ) {
                viewModel.deleteAccount()
            }
        }
        bindDeleteData()
    }

    private fun bindDeleteData() {
        observeNotNull(viewModel.deleteAccountData, { resource ->
            when (resource) {
                is Resource.Loading -> showProgressDialog(getString(R.string.loading_))
                is Resource.Success -> {
                    hideProgressDialog()
                    invalidateAndRestart()
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    invalidateAndRestart()
                }
                else -> {

                }
            }

        })
    }

    private fun bindData() {
        observeNotNull(viewModel.logoutData, { resource ->
            when (resource) {
                is Resource.Loading -> showProgressDialog(getString(R.string.loading_))
                is Resource.Success -> {
                    hideProgressDialog()
                    invalidateAndRestart()
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    invalidateAndRestart()
                }
                else -> {

                }
            }

        })
    }

    fun invalidateAndRestart() {
        session.invalidateSession()
        removeUserIdentification(firebaseAnalytics, firebaseCrashlytics)
        startRootActivity()
    }

    private fun startRootActivity() {
        startActivity(Intent(activity, RootActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    private fun changeAppConfiguration() {
        val application = requireActivity().application
        LocaleHelper(application).createConfigurationContext()
    }
}