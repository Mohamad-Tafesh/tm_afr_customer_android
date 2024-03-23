package com.africell.africell.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.africell.africell.BuildConfig
import com.africell.africell.BuildConfig.FLAVOR
import com.africell.africell.Constant.BASE_URL
import com.africell.africell.app.debugOnly
import com.africell.africell.data.repository.domain.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.EOFException
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
@InstallIn(SingletonComponent::class)
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
        /*.authenticator { route, response ->
            val hasEmptyBody = try {
                response.body?.source()?.peek()?.readUtf8()
                false
            } catch (e: EOFException) {
                true
            }

            if (response.code == 401 && hasEmptyBody) {
                //fixing bug with most apis from Africell server. There is no other way to solve it app-side.
                response.newBuilder()
                    .body(
                        "{\"status\":401,\"title\":\"Invalid_token\"}".toResponseBody()
                    )
                    .build()
            } else {
                response
            }

        }*/

        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor { message -> Timber.tag("OkHttp").v(message) }
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        val block: (chain: Interceptor.Chain) -> Response = {

            var credentials = ""
            credentials = if (FLAVOR == "sl" ) {
                Credentials.basic("sc-afr-sl-api", "s@c_2hg!0m9k")
            }else if (FLAVOR == "drc") {
            Credentials.basic("ProdAPI", "ProdAPIP@ssw0rd")
            } else {
                Credentials.basic("sc-afr-gm-api", "s@c_2hg!0m9k")
            }

            debugOnly {
                //credentials = Credentials.basic("TestingAPI", "TestingAPI", UTF_8)
            }

            //
            val response = it.proceed(
                it.request().let { request ->

                    Timber.tag("OkHttp").d("Request: ${request.url}")
                    Timber.tag("OkHttp").d("BasicToken: ${credentials}")

                    if (FLAVOR == "drc") {
                        request.newBuilder()
                            .header("User-Agent", System.getProperty("http.agent").orEmpty())
                            .header("Content-Type", "application/json")
                            .header("accept", "text/plain")
                            .header("Accept-Language", session.language /*session.language*/)
                            .header("Authorization", credentials)
                    } else {
                        request.newBuilder()
                            .header("User-Agent", System.getProperty("http.agent").orEmpty())
                            .header("Content-Type", "application/json")
                            .header("accept", "text/plain")
                            .header("Accept-Language", "en" /*session.language*/)
                            .header("Authorization", credentials)
                    }


                        .apply {
                            if (
                                request.tag(String::class.java) != ApiContract.Params.NO_TOKEN_TAG &&
                                session.isLoggedIn()
                            ) {
                                header("X-Authorization", "Bearer " + session.accessToken)

                                Timber.tag("OkHttp").d("Access-Token: ${session.accessToken}")

                            }
                        }
                        .build()
                }
            )
            val body = try {
                response.body?.source()?.peek()?.readUtf8()
            } catch (e: EOFException) {
                null
            }
            if (BuildConfig.DEBUG) {
                Timber.tag("OkHttp-test").v("Response: $response")
                Timber.tag("OkHttp-test").v("Response code: ${response.code}")
                Timber.tag("OkHttp-test").v("Response code: ${body.orEmpty()}")
            }
            val hasEmptyBody = try {
                response.body?.source()?.peek()?.readUtf8()
                false
            } catch (e: EOFException) {
                true
            }

            if (response.code == 401 && hasEmptyBody) {
                //fixing bug with most apis from Africell server. There is no other way to solve it app-side.
                response.newBuilder()
                    .body(
                        "{\"status\":401,\"title\":\"Invalid_token\"}".toResponseBody()
                    )
                    .build()
            } else {
                response
            }
        }
        builder.addInterceptor(block)

        return builder.build()
    }


    @Provides
    @Singleton
    @Named("FrescoClient")
    internal fun provideFrescoClient(client: OkHttpClient, session: SessionRepository): OkHttpClient {
        val builder = client.newBuilder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
        //In case Retrofit or OkHttp were updated, this allows relying on TLSv1 and TLSv1.1 in case needed.
        //.connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS))

        builder.addInterceptor {//addNetworkInterceptor in case you don't want to handle redirections, but logs will not show "access-token"

//            val credentials: String = Credentials.basic("TestingAPI", "TestingAPI", UTF_8)
            it.proceed(
                it.request().let { request ->
                    request.newBuilder()
                        .header("User-Agent", System.getProperty("http.agent").orEmpty())
                        .header("Content-Type", "application/json")
                        .header("accept", "text/plain")
                        .header("Accept-Language", session.language)

                        .apply {
                            if (
                                request.tag(String::class.java) != ApiContract.Params.NO_TOKEN_TAG &&
                                session.isLoggedIn()
                            ) {
                                header("X-Authorization", "Bearer " + session.accessToken)

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
            .baseUrl(BASE_URL)
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