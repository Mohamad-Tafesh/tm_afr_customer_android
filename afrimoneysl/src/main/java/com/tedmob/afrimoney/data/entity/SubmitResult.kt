package com.tedmob.afrimoney.data.entity

sealed class SubmitResult(val message: String) {
    class Success(message: String) : SubmitResult(message)
    class Failure(message: String) : SubmitResult(message)
}