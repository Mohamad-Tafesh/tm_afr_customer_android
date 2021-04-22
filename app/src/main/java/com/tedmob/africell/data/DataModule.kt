package com.tedmob.africell.data

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tedmob.africell.data.api.ApiModule
import com.tedmob.africell.data.api.UnsafeOkHttpClient
import com.tedmob.africell.util.log.CrashlyticsExceptionLogger
import com.tedmob.africell.util.log.ExceptionLogger
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
object DataModule {

    private const val DISK_CACHE_SIZE = 20 * 1024 * 1024 // 20 MiB

    @Provides
    @Singleton
    internal fun providesSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    internal fun provideRxSharedPreferences(prefs: SharedPreferences): RxSharedPreferences {
        return RxSharedPreferences.create(prefs)
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpCache(application: Application): Cache {
        return Cache(application.cacheDir, DISK_CACHE_SIZE.toLong())
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return ( /*UnsafeOkHttpClient.getUnsafeOkHttpClient()?.newBuilder() ?:*/
        OkHttpClient.Builder())
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(application)
    }

    @Provides
    @Singleton
    internal fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

    @Provides
    @Singleton
    internal fun provideExceptionLogger(crashlytics: FirebaseCrashlytics): ExceptionLogger {
        return CrashlyticsExceptionLogger(crashlytics)
    }
}