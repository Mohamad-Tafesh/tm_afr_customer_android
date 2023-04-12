package com.tedmob.afrimoney.util.dialog

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


suspend fun ComponentActivity.showConfirmationDialog(
    message: CharSequence,
    positiveButtonText: CharSequence,
    negativeButtonText: CharSequence
): Boolean {
    return suspendCancellableCoroutine { cont ->
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                cont.resume(true)
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                cont.resume(false)
            }
            .setOnDismissListener {
                if (cont.isActive) {
                    cont.resume(false)
                }
            }
            .show()
    }
}

suspend fun ComponentActivity.showAlertDialog(
    message: CharSequence,
    buttonText: CharSequence
): Unit {
    return suspendCancellableCoroutine { cont ->
        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton(buttonText) { _, _ ->
                cont.resume(Unit)
            }
            .setOnDismissListener {
                if (cont.isActive) {
                    cont.resume(Unit)
                }
            }
            .show()
    }
}

//TODO prompt dialog (with input)


suspend fun Fragment.showConfirmationDialog(
    message: CharSequence,
    positiveButtonText: CharSequence,
    negativeButtonText: CharSequence
): Boolean {
    return suspendCancellableCoroutine { cont ->
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { _, _ ->
                    cont.resume(true)
                }
                .setNegativeButton(negativeButtonText) { _, _ ->
                    cont.resume(false)
                }
                .setOnDismissListener {
                    if (cont.isActive) {
                        cont.resume(false)
                    }
                }
                .show()
        } ?: cont.cancel()
    }
}

suspend fun Fragment.showAlertDialog(
    message: CharSequence,
    buttonText: CharSequence
): Unit {
    return suspendCancellableCoroutine { cont ->
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(message)
                .setPositiveButton(buttonText) { _, _ ->
                    cont.resume(Unit)
                }
                .setOnDismissListener {
                    if (cont.isActive) {
                        cont.resume(Unit)
                    }
                }
                .show()
        } ?: cont.cancel()
    }
}