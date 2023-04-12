package com.tedmob.afrimoney.ui.blocks

import androidx.annotation.DrawableRes

fun LoadingLayout.showLoading() {
    showLoadingView()
    loadingView.loading(true)
}

fun LoadingLayout.showMessage(message: CharSequence) {
    showLoadingView()
    loadingView.loading(false)
        .message(message)
        .displayImage(false)
        .displayButton(false)
}

fun LoadingLayout.showImage(@DrawableRes resId: Int) {
    showLoadingView()
    loadingView.loading(false)
        .message("")
        .imageResource(resId)
        .displayImage(true)
        .displayButton(false)
}

fun LoadingLayout.showMessageWithAction(message: CharSequence, actionName: String, action: (() -> Unit)?) {
    showLoadingView()
    loadingView.loading(false)
        .message(message)
        .displayButton(true)
        .buttonText(actionName)
        .buttonClickListener { action?.invoke() }
}