package com.africell.africell.util.dagger

import android.content.Context
import dagger.android.support.DaggerApplication

abstract class MultiDexDaggerApplication : DaggerApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
    }
}