package com.tedmob.afrimoney.exception

class AppException : Exception {
    object Code {
        const val NETWORK = -1000
        const val DATA = -1001
        const val UNEXPECTED = -1002

        const val INVALID_TOKEN = 401
        const val INVALID_PIN = -1
    }

    var status: Int = 400
    var code: Int = Code.UNEXPECTED
    var userMessage: CharSequence = ""
    var developerMessage: String? = null

    constructor(message: CharSequence?) : super(message?.toString()) {
        userMessage = message ?: ""
    }

    constructor(
        code: Int,
        message: CharSequence,
        developerMessage: String?
    ) : super(message.toString()) {
        this.userMessage = message
        this.code = code
        this.developerMessage = developerMessage
    }

    constructor(message: CharSequence?, cause: Throwable?) : super(message?.toString(), cause) {
        userMessage = message ?: ""
    }

    constructor(
        code: Int,
        message: CharSequence,
        developerMessage: String?,
        cause: Throwable?
    ) : super(message.toString(), cause) {
        this.userMessage = message
        this.code = code
        this.developerMessage = developerMessage
    }

    constructor(
        message: CharSequence?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message?.toString(), cause, enableSuppression, writableStackTrace) {
        userMessage = message ?: ""
    }

    constructor(cause: Throwable?) : super(cause)


    override fun toString(): String {
        return "AppException(code=$code, message=$message, developerMessage=$developerMessage)"
    }
}