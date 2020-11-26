package com.tedmob.africell.exception

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.tedmob.africell.R
import com.tedmob.africell.app.StringLoader
import com.tedmob.africell.data.api.dto.ErrorDTO
import com.tedmob.africell.exception.AppException.Code.DATA
import com.tedmob.africell.exception.AppException.Code.NETWORK
import com.tedmob.africell.exception.AppException.Code.UNEXPECTED
import com.tedmob.africell.util.log.ExceptionLogger
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLHandshakeException

@Singleton
class AppExceptionFactory
@Inject constructor(
    strings: StringLoader,
    @field:[Named("Api")] private val gson: Gson,
    private val logger: ExceptionLogger
) {
    private val networkErrorMessage by lazy { strings.getString(R.string.error_network) }
    private val unexpectedErrorMessage by lazy { strings.getString(R.string.error_unexpected) }

    fun make(t: Throwable): AppException {
        if (t is AppException) return t

        return when (t) {
            is HttpException -> {
                var errorResponse: String? = null
                try {
                    errorResponse = t.response()?.errorBody()?.string()
                    val apiResponse = gson.fromJson(errorResponse, ErrorDTO::class.java)
                    val appException = AppException(
                        apiResponse.status ?: UNEXPECTED,
                        apiResponse.title ?: unexpectedErrorMessage,
                        apiResponse.title, t
                    )

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

}