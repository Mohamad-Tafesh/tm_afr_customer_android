package com.tedmob.afrimoney.app

import com.tedmob.afrimoney.BuildConfig

inline fun debugOnly(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}