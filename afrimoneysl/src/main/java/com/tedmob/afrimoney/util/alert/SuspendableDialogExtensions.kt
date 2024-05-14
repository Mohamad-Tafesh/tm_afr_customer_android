@file:Suppress("RedundantUnitReturnType", "unused")

package com.tedmob.afrimoney.util.alert

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun AndroidComponent.showConfirmationDialog(
    message: CharSequence,
    positiveButtonText: CharSequence,
    negativeButtonText: CharSequence,
): Boolean {
    return suspendCancellableCoroutine { cont ->
        androidContext?.showMaterialAlert {
            setMessage(message)
            setPositiveButton(positiveButtonText) { _, _ ->
                cont.resume(true)
            }
            setNegativeButton(negativeButtonText) { _, _ ->
                cont.resume(false)
            }
            setOnCancelListener {
                if (cont.isActive) {
                    cont.resume(false)
                }
            }
        } ?: cont.cancel()
    }
}

suspend fun AndroidComponent.showAlertDialog(
    message: CharSequence,
    buttonText: CharSequence,
): Unit {
    return suspendCancellableCoroutine { cont ->
        androidContext?.showMaterialAlert {
            setMessage(message)
            setPositiveButton(buttonText) { _, _ ->
                cont.resume(Unit)
            }
            setOnCancelListener {
                if (cont.isActive) {
                    cont.resume(Unit)
                }
            }
        } ?: cont.cancel()
    }
}

//TODO prompt dialog (with input)

suspend fun <T> AndroidComponent.showDialogForResult(
    builder: MaterialAlertDialogBuilder.(continuation: CancellableContinuation<T>) -> Unit,
): T {
    return suspendCancellableCoroutine { cont ->
        androidContext?.showMaterialAlert {
            setOnCancelListener { cont.cancel() }
            builder(cont)
        } ?: cont.cancel()
    }
}

