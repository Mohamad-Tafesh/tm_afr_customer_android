package com.tedmob.africell.util.preference

import com.f2prateek.rx.preferences2.Preference
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class RxPrefObjectProperty<T>(
    private var pref: Preference<String>,
    private val gson: Gson,
    private val objectClass: Class<T>
) : ReadWriteProperty<Any?, T?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val jsonObject = pref.get()
        return if (jsonObject.isBlank()) null else gson.fromJson(jsonObject, objectClass)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        pref.set(if (value == null) "" else gson.toJson(value, objectClass))
    }
}