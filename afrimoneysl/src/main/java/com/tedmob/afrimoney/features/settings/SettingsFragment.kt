package com.tedmob.afrimoney.features.settings

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onesignal.OneSignal
import com.tedmob.afrimoney.BuildConfig
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import com.tedmob.afrimoney.databinding.DialogLogoutBinding
import com.tedmob.afrimoney.databinding.FragmentSettingsBinding
import com.tedmob.afrimoney.features.launch.RootActivity
import com.tedmob.afrimoney.util.locale.LocaleHelper
import com.tedmob.afrimoney.util.removeUserIdentification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseVBFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var crashlytics: CrashlyticsHandler


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentSettingsBinding::inflate)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.title = getString(R.string.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOneSignalStateTo(requireBinding().notificationsLayout, requireBinding().notificationsSwitch)
        setupLanguage()
        bindAppVersionTo(requireBinding().appVersionLayout, requireBinding().appVersionSummaryText)
        setupLoginLogout()
    }

    private fun setupLanguage() {
        if (BuildConfig.MULTI_LANG) {
            requireBinding().languageLayout.visibility = View.VISIBLE
            requireBinding().languageSeparator.visibility = View.VISIBLE
            bindLanguageMenuTo(
                requireBinding().languageLayout,
                requireBinding().languageTitleText,
                requireBinding().languageSummaryText,
                mode = LanguageMenuMode.DIALOG
            ) { popup, language, code ->
                popup?.dismiss()

                if (code != session.language) {
                    session.userChangedLanguage = true
                    OneSignal.setLanguage(code)

                    //fixme Force applying preference change before system does in order to change app resources' configuration
                    session.language = code
                    changeAppConfiguration()

                    startRootActivity()
                }
            }
        } else {
            requireBinding().languageLayout.visibility = View.GONE
            requireBinding().languageSeparator.visibility = View.GONE
        }
    }

    private fun setupLoginLogout() {
        //loginLogoutTitleText.setText(if (session.isLoggedIn()) R.string.logout else R.string.login)
        requireBinding().loginLogoutLayout.visibility = if (session.isLoggedIn()) View.VISIBLE else View.GONE
        requireBinding().loginLogoutLayout.setOnClickListener {
showLogoutDialog()
        }
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

    private fun showLogoutDialog() {
        this.let {
            val viewBinding = DialogLogoutBinding.inflate(
                it.layoutInflater,
                FrameLayout(requireContext()),
                false
            )

            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertBottom)
                .setView(viewBinding.root)
                .setOnDismissListener {}
                .show()


            viewBinding.run {
                closeButton.setOnClickListener {
                    dialog.dismiss()
                    session.invalidateSession()
                    removeUserIdentification(analytics, crashlytics)
                    startRootActivity()
                }
            }

            val window = dialog.window
            val wlp = window?.attributes
            wlp?.gravity = Gravity.BOTTOM;
            wlp?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            window?.setAttributes(wlp)
        }
    }
}