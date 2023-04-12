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
    var userMessage: String = ""
    var developerMessage: String? = null

    constructor(message: String?) : super(message) {
        userMessage = message ?: ""
    }

    constructor(
        code: Int,
        message: String,
        developerMessage: String?
    ) : super(message) {
        this.userMessage = message
        this.code = code
        this.developerMessage = developerMessage
    }

    constructor(message: String?, cause: Throwable?) : super(message, cause) {
        userMessage = message ?: ""
    }

    constructor(
        code: Int,
        message: String,
        developerMessage: String?,
        cause: Throwable?
    ) : super(message, cause) {
        this.userMessage = message
        this.code = code
        this.developerMessage = developerMessage
    }

    constructor(cause: Throwable?) : super(cause)


    override fun toString(): String {
        return "AppException(code=$code, message=$message, developerMessage=$developerMessage)"
    }
}