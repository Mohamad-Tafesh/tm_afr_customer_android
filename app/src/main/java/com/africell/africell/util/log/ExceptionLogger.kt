package com.africell.africell.util.log

interface ExceptionLogger {
    fun saveLog(message: String)
    fun logException(throwable: Throwable)
}