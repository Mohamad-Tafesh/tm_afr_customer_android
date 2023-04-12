package com.tedmob.afrimoney.data.entity

sealed interface UserState {
    class Registered(
        val user: User
    ) : UserState

    class NotRegistered(
        val token: String
    ) : UserState
}