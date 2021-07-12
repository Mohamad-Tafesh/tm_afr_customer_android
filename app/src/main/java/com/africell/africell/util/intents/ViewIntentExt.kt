@file:JvmName("ViewIntentUtils")

package com.africell.africell.util.intents

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Browser
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import java.io.File
import androidx.fragment.app.Fragment as SupportFragment

//Intents:

fun getViewIntent(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url))

fun getViewIntent(file: File) = Intent(Intent.ACTION_VIEW, Uri.fromFile(file))

fun getViewIntentFromProvider(context: Context, file: File, providerName: String = "${context.packageName}.provider") =
    Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(context, providerName, file))

fun getViewIntentWithType(url: String, type: String): Intent {
    val uri = Uri.parse(url)
    return Intent(Intent.ACTION_VIEW, uri).apply { setDataAndType(uri, type) }
}

fun getViewIntentWithType(file: File, type: String): Intent {
    val uri = Uri.fromFile(file)
    return Intent(Intent.ACTION_VIEW, uri).apply { setDataAndType(uri, type) }
}

fun getViewIntentWithTypeFromProvider(
    context: Context,
    file: File,
    type: String,
    providerName: String = "${context.packageName}.provider"
): Intent {
    val uri = FileProvider.getUriForFile(context, providerName, file)
    return Intent(Intent.ACTION_VIEW, uri).apply { setDataAndType(uri, type) }
}


fun Context.getViewIntent(@StringRes urlRes: Int) = resources.getViewIntent(urlRes)

fun Context.getViewIntentWithType(@StringRes urlRes: Int, type: String) = resources.getViewIntentWithType(urlRes, type)

fun Context.getViewIntentWithProvider(file: File, providerName: String = "$packageName.provider") =
    getViewIntentFromProvider(this, file, providerName)

fun Context.getViewIntentWithTypeAndProvider(file: File, type: String, providerName: String = "$packageName.provider") =
    getViewIntentWithTypeFromProvider(this, file, type, providerName)

fun Context.getWebsiteViewIntent(url: String) = getViewIntent(url).apply {
    addApplicationId(packageName)
}

fun Context.getWebsiteViewIntent(@StringRes urlRes: Int) = getViewIntent(urlRes).apply {
    addApplicationId(packageName)
}


fun SupportFragment.getViewIntent(@StringRes urlRes: Int) = resources.getViewIntent(urlRes)

fun SupportFragment.getViewIntentWithType(@StringRes urlRes: Int, type: String) =
    resources.getViewIntentWithType(urlRes, type)

fun SupportFragment.getViewIntentWithProvider(file: File, providerName: String = "${activity?.packageName}.provider") =
    getViewIntentFromProvider(requireNotNull(activity) { "Activity of support fragment is null." }, file, providerName)

fun SupportFragment.getViewIntentWithTypeAndProvider(
    file: File,
    type: String,
    providerName: String = "${activity?.packageName}.provider"
) =
    getViewIntentWithTypeFromProvider(
        requireNotNull(activity) { "Activity of fragment is null." },
        file,
        type,
        providerName
    )

fun SupportFragment.getWebsiteViewIntent(url: String) = getViewIntent(url).apply {
    activity?.let { addApplicationId(it.packageName) }
}

fun SupportFragment.getWebsiteViewIntent(@StringRes urlRes: Int) = getViewIntent(urlRes).apply {
    activity?.let { addApplicationId(it.packageName) }
}


fun Resources.getViewIntent(@StringRes urlRes: Int) = getViewIntent(getString(urlRes))

fun Resources.getViewIntentWithType(@StringRes urlRes: Int, type: String) =
    getViewIntentWithType(getString(urlRes), type)


private fun Intent.addApplicationId(id: String) {
    putExtra(Browser.EXTRA_APPLICATION_ID, id)
}