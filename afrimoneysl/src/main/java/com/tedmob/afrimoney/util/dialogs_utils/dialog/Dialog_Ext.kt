package com.tedmob.afrimoney.util.dialogs_utils.dialog

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tedmob.taxibeirut.user.my_libs.dialogs_utils.dialog.DialogHelper


internal inline fun <DVB : ViewBinding> Fragment.showVBDialog(
    noinline bindingProvider: (inflater: LayoutInflater) -> DVB,
    block: DialogHelper<DVB>.() -> Unit,
) = block.invoke(DialogHelper(requireContext(), bindingProvider))

internal inline fun <DVB : ViewBinding> Activity.showVBDialog(
    noinline bindingProvider: (inflater: LayoutInflater) -> DVB,
    block: DialogHelper<DVB>.() -> Unit,
) = block.invoke(DialogHelper(this, bindingProvider))

internal inline fun <DVB : ViewBinding> Context.showVBDialog(
    noinline bindingProvider: (inflater: LayoutInflater) -> DVB,
    block: DialogHelper<DVB>.() -> Unit,
) = block.invoke(DialogHelper(this, bindingProvider))

