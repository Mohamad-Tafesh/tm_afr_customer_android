package com.tedmob.afrimoney.exception

import android.app.Application
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.data.api.dto.ErrorDTO
import com.tedmob.afrimoney.exception.AppException.Code.DATA
import com.tedmob.afrimoney.exception.AppException.Code.NETWORK
import com.tedmob.afrimoney.exception.AppException.Code.UNEXPECTED
import com.tedmob.afrimoney.util.html.html
import com.tedmob.afrimoney.util.log.ExceptionLogger
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLHandshakeException

@Singleton
class AppExceptionFactory
@Inject constructor(
    private val app: Application,
    @Named("Api") private val gson: Gson,
    private val logger: ExceptionLogger
) {
    private val networkErrorMessage get() = app.getString(R.string.error_network)
    private val unexpectedErrorMessage get() = app.getString(R.string.error_unexpected)


    fun make(t: Throwable): AppException =
        when (t) {
            is AppException -> t

            is HttpException -> {
                var errorResponse: String? = null
                try {
                    val response = t.response()
                    val errorBody = response?.errorBody()
                    val contentType = errorBody?.contentType()
                    errorResponse = errorBody?.string()

                    val appException: AppException =
                        when {
                            contentType?.type == "application" && contentType.subtype == "json" -> {
                                when {
                                    errorResponse != null -> {
                                        val apiResponse = gson.fromJson(errorResponse, ErrorDTO::class.java)
                                        apiResponse.error.let {
                                            AppException(
                                                it?.code ?: UNEXPECTED,
                                                it?.message ?: unexpectedErrorMessage,
                                                it?.debugger,
                                                t
                                            ).apply {
                                                status = response.code()
                                            }
                                        }
                                    }
                                    response.code() == 401 -> {
                                        AppException(
                                            AppException.Code.INVALID_TOKEN,
                                            "Invalid Token",
                                            null,
                                            t
                                        ).apply {
                                            status = response.code()
                                        }
                                    }
                                    else -> {
                                        AppException(
                                            UNEXPECTED,
                                            unexpectedErrorMessage,
                                            null,
                                            t,
                                        ).apply {
                                            status = response.code()
                                        }
                                    }
                                }
                            }
                            contentType?.type == "text" && contentType.subtype == "html" -> {
                                AppException(
                                    UNEXPECTED,
                                    errorResponse?.html() ?: unexpectedErrorMessage,
                                    errorResponse,
                                    t
                                ).apply {
                                    status = response.code()
                                }
                            }
                            else -> {
                                AppException(
                                    UNEXPECTED,
                                    errorResponse ?: unexpectedErrorMessage,
                                    errorResponse,
                                    t
                                ).apply {
                                    status = response?.code() ?: 400
                                }
                            }
                        }

                    logger.saveLog("Api call url:\n${t.response()?.raw()?.request?.url}")
                    logger.logException(appException)

                    appException
                } catch (e: IOException) {
                    val appException = AppException(DATA, unexpectedErrorMessage, t.message, e)

                    logger.saveLog("Api call url:\n${t.response()?.raw()?.request?.url}")
                    logger.logException(appException)

                    appException
                } catch (e: JsonSyntaxException) {
                    val appException = AppException(DATA, unexpectedErrorMessage, t.message, e)

                    logger.saveLog("Api call url:\n${t.response()?.raw()?.request?.url}")
                    logger.saveLog("Api call response:\n$errorResponse")
                    logger.logException(appException)

                    appException
                }
            }

            is IOException -> {
                if (t is SSLHandshakeException) {
                    logger.logException(t)
                }

                AppException(NETWORK, networkErrorMessage, t.message, t)
            }

            is JsonParseException -> {
                logger.logException(t)

                AppException(UNEXPECTED, "The server's response was not expected", t.message, t)//TODO proper message
            }

            else -> {
                logger.logException(t)

                AppException(UNEXPECTED, unexpectedErrorMessage, t.message, t)
            }
        }
}