package com.tedmob.afrimoney.util.log

interface ExceptionLogger {
    fun saveLog(message: String)
    fun logException(throwable: Throwable)
}