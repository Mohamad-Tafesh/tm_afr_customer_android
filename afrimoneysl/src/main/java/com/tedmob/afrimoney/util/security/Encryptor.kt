package com.tedmob.afrimoney.util.security

interface Encryptor<T> {
    fun encrypt(value: T): String
    fun decrypt(value: String): T
}

typealias StringEncryptor = Encryptor<String>


inline fun StringEncryptor.doNotHandleEmpty(): StringEncryptor =
    object : StringEncryptor {
        override fun encrypt(value: String): String =
            if (value.isNotEmpty()) this@doNotHandleEmpty.encrypt(value) else ""

        override fun decrypt(value: String): String =
            if (value.isNotEmpty()) this@doNotHandleEmpty.decrypt(value) else ""
    }

inline fun StringEncryptor.safe(): StringEncryptor =
    object : StringEncryptor {
        override fun encrypt(value: String): String {
            return try {
                this@safe.encrypt(value)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        override fun decrypt(value: String): String {
            return try {
                this@safe.decrypt(value)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }