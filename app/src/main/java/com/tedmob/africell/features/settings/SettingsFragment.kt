package com.tedmob.africell.features.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.launch.RootActivity
import com.tedmob.africell.ui.viewmodel.observeNotNull
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.locale.LocaleHelper
import com.tedmob.africell.util.removeUserIdentification
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

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
        return wrap(inflater.context, R.layout.fragment_settings,R.layout.toolbar_default)
    }

    override fun configureToolbar() {
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupImageBanner(imageView, ApiContract.Params.BANNERS, ApiContract.ImagePageName.SETTINGS )
        bindOneSignalStateTo(notificationsLayout, notificationsSwitch)
        setupLanguage()
        bindAppVersionTo(appVersionLayout, appVersionSummaryText)
        setupLoginLogout()
    }

    private fun setupLanguage() {
      /*  if (BuildConfig.MULTI_LANG) {
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
        }*/
    }

    private fun setupLoginLogout() {
        //loginLogoutTitleText.setText(if (session.isLoggedIn()) R.string.logout else R.string.login)
        loginLogoutLayout.visibility = if (session.isLoggedIn()) View.VISIBLE else View.GONE
        loginLogoutLayout.setOnClickListener {
            if(session.isLoggedIn()){
                viewModel.logout()
            }else {
                session.invalidateSession()
                removeUserIdentification(firebaseAnalytics, firebaseCrashlytics)
                startRootActivity()
            }
        }
        bindData()
    }

    private fun bindData(){
        observeNotNull(viewModel.logoutData,{
                resource ->
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
            }

        })
    }

    fun invalidateAndRestart(){
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