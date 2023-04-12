package com.tedmob.afrimoney.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * This version allows multiple observers.
 */
class SingleLiveEvent<T> : MutableLiveData<T> {

    private val pendingEvents: MutableMap<ObserverDelegate<in T>, AtomicBoolean> = mutableMapOf()


    constructor() : super()
    constructor(value: T) : super(value)


    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val observerDelegate = synchronized(pendingEvents) {
            val delegate = ObserverDelegate(pendingEvents.size, observer, shouldUpdate = {
                pendingEvents[it]?.compareAndSet(true, false) == true
            })
            pendingEvents[delegate] = AtomicBoolean(false)
            delegate
        }

        super.observe(owner, observerDelegate)
    }

    override fun setValue(t: T?) {
        pendingEvents.forEach { it.value.set(true) }
        super.setValue(t)
    }

    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(observer)
        pendingEvents.remove(observer as ObserverDelegate<in T>)
    }

    override fun onInactive() {
        if (!hasObservers()) {
            pendingEvents.clear()
        }
    }


    private class ObserverDelegate<T>(
        val id: Int,
        val observer: Observer<in T>,
        val shouldUpdate: (ObserverDelegate<T>) -> Boolean
    ) : Observer<T> {

        override fun onChanged(t: T) {
            if (shouldUpdate(this)) {
                observer.onChanged(t)
            }
        }

        override fun equals(other: Any?): Boolean = other is ObserverDelegate<*> && this.id == other.id

        override fun hashCode(): Int = id.hashCode()
    }
}
