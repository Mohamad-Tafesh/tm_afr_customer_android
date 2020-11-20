package com.tedmob.africell.features.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.onesignal.OneSignal
import com.tedmob.africell.R
import com.tedmob.africell.features.settings.menu.SettingsMenuPopupWindow

fun bindOneSignalStateTo(container: ViewGroup, switch: SwitchCompat) {
    switch.isChecked =
        OneSignal.getPermissionSubscriptionState()?.subscriptionStatus?.subscribed == true

    container.setOnClickListener {
        OneSignal.setSubscription(!switch.isChecked)
        switch.toggle()
    }
}

enum class LanguageMenuMode {
    DROPDOWN,
    DIALOG
}

class Dismissable(private val delegate: Any?) {

    fun dismiss() {
        when (delegate) {
            is PopupWindow? -> delegate?.dismiss()
            is DialogInterface? -> delegate?.dismiss()
        }
    }
}

inline fun Any.dismissablePopup() = Dismissable(this)

fun SettingsFragment.bindLanguageMenuTo(
    container: ViewGroup,
    titleText: View,
    languageChosenText: TextView?,
    mode: LanguageMenuMode = LanguageMenuMode.DROPDOWN,
    onLanguageSelected: (popup: Dismissable?, language: String, code: String) -> Unit
) {
    val languages = resources.getStringArray(R.array.supported_languages)
    val languageCodes = resources.getStringArray(R.array.supported_language_codes)
    val selectedIndex = languageCodes.indexOfFirst { it == session.language }
        .takeIf { it != -1 }
        ?: 0

    languageChosenText?.text = languages[selectedIndex]

    when (mode) {
        LanguageMenuMode.DROPDOWN -> {
            var languagePopupWindow: SettingsMenuPopupWindow? = null

            container.setOnClickListener {
                languagePopupWindow = SettingsMenuPopupWindow(layoutInflater.context)
                    .apply {
                        entries = languages.toList()
                        this.selectedIndex = selectedIndex
                        onItemClickListener = {
                            onLanguageSelected(
                                languagePopupWindow?.dismissablePopup(),
                                languages[it],
                                languageCodes[it]
                            )
                        }
                    }
                languagePopupWindow?.show(container, view!!, titleText.x.toInt())
            }

            viewLifecycleOwner.lifecycle.addObserver(
                object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroyed() {
                        languagePopupWindow?.dismiss()
                    }
                }
            )
        }

        LanguageMenuMode.DIALOG -> {
            activity?.let { activity ->
                val adapter = ArrayAdapter(activity, android.R.layout.select_dialog_singlechoice, languages)
                    .apply { setDropDownViewResource(android.R.layout.select_dialog_singlechoice) }

                var dialog: AlertDialog? = null

                container.setOnClickListener {
                    dialog = MaterialAlertDialogBuilder(activity)
                        .setSingleChoiceItems(adapter, selectedIndex) { dialog, which ->
                            onLanguageSelected(dialog?.dismissablePopup(), languages[which], languageCodes[which])
                        }
                        .show()
                }

                viewLifecycleOwner.lifecycle.addObserver(
                    object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                        fun onDestroyed() {
                            dialog?.dismiss()
                        }
                    }
                )
            }
        }
    }
}

fun SettingsFragment.bindAppVersionTo(container: ViewGroup, appVersionText: TextView?) {
    container.isLongClickable = true
    container.setOnCreateContextMenuListener { menu, _, _ ->
        val versionName = appVersionText?.text?.toString() ?: getString(R.string.app_version_name)

        menu.setHeaderTitle(versionName)
            .add(
                Menu.NONE,
                Menu.NONE,
                Menu.NONE,
                R.string.copy
            )
            .setOnMenuItemClickListener {
                val clipboard =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("App Version Name", versionName)

                clipboard.setPrimaryClip(clip)

                Toast.makeText(
                    requireContext(),
                    getString(R.string.preference_copied, versionName),
                    Toast.LENGTH_SHORT
                ).show()

                true
            }
    }
}