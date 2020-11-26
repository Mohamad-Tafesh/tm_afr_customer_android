package com.tedmob.africell.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tedmob.africell.BuildConfig
import com.tedmob.africell.data.repository.domain.SessionRepository
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import timber.log.Timber
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides
    @Singleton
    @Named("Api")
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Provides
    @Singleton
    @Named("Api")
    internal fun provideApiClient(client: OkHttpClient, session: SessionRepository): OkHttpClient {
        val builder = client.newBuilder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
        //In case Retrofit or OkHttp were updated, this allows relying on TLSv1 and TLSv1.1 in case needed.
        //.connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS))

        builder.addInterceptor {//addNetworkInterceptor in case you don't want to handle redirections, but logs will not show "access-token"
            val credentials: String = Credentials.basic("TestingAPI", "TestingAPI", UTF_8)
            it.proceed(
                it.request().let { request ->
                    request.newBuilder()
                        .header("User-Agent", System.getProperty("http.agent").orEmpty())
                        .header("Content-Type", "application/json")
                        .header("Accept-Language", "en")
                        .header("Authorization", credentials)

                        .apply {
                            if (
                                request.tag(String::class.java) != ApiContract.Params.NO_TOKEN_TAG &&
                                session.isLoggedIn()
                            ) {
                                header("access-token", session.accessToken)

                            }
                        }
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

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        @Named("Api") client: OkHttpClient,
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
    internal fun provideRestApi(retrofit: Retrofit): RestApi {
        return retrofit.create()
    }
}