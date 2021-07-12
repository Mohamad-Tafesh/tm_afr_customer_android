package com.africell.africell.app

import android.content.Context
import android.content.res.Configuration
import android.webkit.WebView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.onesignal.OneSignal
import com.africell.africell.BuildConfig
import com.africell.africell.R
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.notification.NotificationOpenedHandler
import com.africell.africell.util.identifyUser
import com.africell.africell.util.locale.LocaleHelper
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


class App : DaggerApplication() {

    @Inject
    @Named("FrescoClient")
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var session: SessionRepository

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var firebaseCrashlytics: FirebaseCrashlytics

    private var localeHelper: LocaleHelper? = null

    override fun attachBaseContext(base: Context) {
        if (BuildConfig.MULTI_LANG) {
            localeHelper = LocaleHelper(base)
            super.attachBaseContext(localeHelper!!.createConfigurationContext())
        } else {
            super.attachBaseContext(base)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeHelper?.createConfigurationContext()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            WebView.setWebContentsDebuggingEnabled(true)
        }

        Timber.plant(CrashlyticsTree(firebaseCrashlytics))
        session.identifyUser(firebaseAnalytics, firebaseCrashlytics)
        initFrescoWithOkHttp()
        initOneSignal()
    }

    private fun initFrescoWithOkHttp() {
        val config = OkHttpImagePipelineConfigFactory
            .newBuilder(this, okHttpClient)
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)
    }

    private fun initOneSignal() {
        OneSignal.setAppId(getString(R.string.onesignal_app_id))
        OneSignal.initWithContext(this)
        OneSignal.setNotificationOpenedHandler(NotificationOpenedHandler(this))
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true)
        OneSignal.setLocationShared(false)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)
}