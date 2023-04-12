package com.tedmob.afrimoney.data

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tedmob.afrimoney.data.analytics.AnalyticsHandler
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import com.tedmob.afrimoney.util.log.CrashlyticsExceptionLogger
import com.tedmob.afrimoney.util.log.ExceptionLogger
import com.tedmob.afrimoney.util.phone.PhoneNumberHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideExceptionLogger(crashlytics: CrashlyticsHandler): ExceptionLogger {
        return CrashlyticsExceptionLogger(crashlytics)
    }

    @Provides
    @Singleton
    internal fun provideAnalyticsHandler(application: Application): AnalyticsHandler = AnalyticsHandler[application]

    @Provides
    @Singleton
    internal fun provideCrashlyticsHandler(application: Application): CrashlyticsHandler =
        CrashlyticsHandler[application]

    @Provides
    @Singleton
    internal fun providePhoneNumberUtil(application: Application): PhoneNumberUtil =
        PhoneNumberUtil.createInstance(application)

    @Provides
    @Singleton
    internal fun providePhoneNumberHelper(phoneNumberUtil: PhoneNumberUtil): PhoneNumberHelper =
        PhoneNumberHelper(true, phoneNumberUtil)
}