package com.tedmob.afrimoney.util.preference

import com.f2prateek.rx.preferences2.Preference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class RxPrefProperty<T: Any>(private val pref: Preference<T>) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = pref.get()
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = pref.set(value)
}