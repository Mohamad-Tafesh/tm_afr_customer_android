package com.tedmob.afrimoney.util.preference

import com.f2prateek.rx.preferences2.Preference
import com.tedmob.afrimoney.util.security.Encryptor
import com.tedmob.afrimoney.util.security.decryptWith
import com.tedmob.afrimoney.util.security.encryptWith
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class RxPrefEncryptedProperty<T: Any>(
    private val pref: Preference<String>,
    private val encryptor: Encryptor<T>,
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = pref.get().decryptWith(encryptor)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = pref.set(value.encryptWith(encryptor))
}