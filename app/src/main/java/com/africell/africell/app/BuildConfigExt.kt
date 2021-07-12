package com.africell.africell.app

import com.africell.africell.BuildConfig

inline fun debugOnly(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

inline fun needsUpdate(serverVersion: Int): Boolean = serverVersion > BuildConfig.VERSION_CODE