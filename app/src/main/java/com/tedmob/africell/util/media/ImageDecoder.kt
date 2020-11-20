package com.tedmob.africell.util.media

import android.net.Uri

interface ImageDecoder {
    fun decode(imageUri: Uri)

    interface Callback<in T> {
        fun onStarted()
        fun onFinished(result: T?)
        fun onError(t: Throwable)
    }
}