package com.tedmob.afrimoney.util.preference

import com.f2prateek.rx.preferences2.Preference
import com.google.gson.Gson
import com.tedmob.afrimoney.util.security.StringEncryptor
import com.tedmob.afrimoney.util.security.decryptWith
import com.tedmob.afrimoney.util.security.encryptWith
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class RxPrefEncryptedObjectProperty<T>(
    private val pref: Preference<String>,
    private val gson: Gson,
    private val objectClass: Class<T>,
    private val encryptor: StringEncryptor,
) : ReadWriteProperty<Any?, T?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val jsonObject = pref.get().decryptWith(encryptor)
        return if (jsonObject.isBlank()) null else gson.fromJson(jsonObject, objectClass)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        pref.set(if (value == null) "" else gson.toJson(value, objectClass).encryptWith(encryptor))
    }
}