package com.tedmob.afrimoney.util.security

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> encrypted(encryptor: Encryptor<T>, defaultValue: T) =
    object : ReadWriteProperty<Any?, T> {

        private var value: String = ""


        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return this.value.decryptWith(encryptor)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = value.encryptWith(encryptor)
        }
    }