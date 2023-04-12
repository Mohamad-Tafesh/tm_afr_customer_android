package com.tedmob.afrimoney.data.api

import com.google.gson.Gson
import com.tedmob.afrimoney.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import timber.log.Timber
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    @Named("Afrimoney")
    internal fun provideApiClient(client: OkHttpClient): OkHttpClient {
        val builder = client.newBuilder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
        //This allows relying on TLSv1 and TLSv1.1 in case needed.
        //.connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS))

        builder.addInterceptor {//addNetworkInterceptor in case you don't want to handle redirections, but logs will not show "access-token"
            it.proceed(
                it.request().let { request ->
                    request.newBuilder()
                        .header("User-Agent", System.getProperty("http.agent").orEmpty())
                        .build()
                }
            )
        }

        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor { message -> Timber.tag("OkHttp").v(message) }
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        builder.ignoreAllSSLErrors()
        return builder.build()
    }

    private fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
        val naiveTrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
            val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        hostnameVerifier(HostnameVerifier { _, _ -> true })
        return this
    }

    @Provides
    @Singleton
    @Named("Afrimoney")
    internal fun provideRetrofit(
        @Named("Afrimoney") client: OkHttpClient,
        @Named("Api") gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(ApiContract.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
    }

    @Provides
    @Singleton
    internal fun provideRestApi(@Named("Afrimoney") retrofit: Retrofit): AfrimoneyRestApi {
        return retrofit.create()
    }
}