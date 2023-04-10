package com.africell.africell.data

import androidx.lifecycle.MutableLiveData


sealed class Resource<out T> {

    class Loading<T>(val data: T? = null) : Resource<T>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error<T>(
        val message: String,
        val action: (() -> Unit)? = null
    ) : Resource<T>()
}


inline fun <reified T> MutableLiveData<Resource<T>>.emitLoading(data: T? = null) {
    postValue(Resource.Loading(data))
}

inline fun <reified T> MutableLiveData<Resource<T>>.emitError(
    message: String,
    noinline action: (() -> Unit)? = null
) {
    postValue(Resource.Error(message, action))
}

inline fun <reified T> MutableLiveData<Resource<T>>.emitSuccess(data: T) {
    postValue(Resource.Success(data))
}