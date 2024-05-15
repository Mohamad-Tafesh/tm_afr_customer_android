package com.tedmob.afrimoney.util.dialogs_utils.dialog_bottom

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


inline fun <DVB : ViewBinding> Fragment.showBottomDialogVB(
    noinline bindingProvider: (inflater: LayoutInflater) -> DVB,
    block: DialogBottomHelper<DVB>.() -> Unit,
) = block.invoke(DialogBottomHelper(requireContext(), bindingProvider))

inline fun <DVB : ViewBinding> Activity.showBottomDialogVB(
    noinline bindingProvider: (inflater: LayoutInflater) -> DVB,
    block: DialogBottomHelper<DVB>.() -> Unit,
) = block.invoke(DialogBottomHelper(this, bindingProvider))

inline fun <DVB : ViewBinding> Context.showBottomDialogVB(
    noinline bindingProvider: (inflater: LayoutInflater) -> DVB,
    block: DialogBottomHelper<DVB>.() -> Unit,
) = block.invoke(DialogBottomHelper(this, bindingProvider))

