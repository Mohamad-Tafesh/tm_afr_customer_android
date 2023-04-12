package com.tedmob.afrimoney.data

import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.data.entity.UserState


sealed class Resource<out T> {

    class Loading<T>(val data: T? = null) : Resource<T>()

    data class Success<T>(val data: T) : Resource<T>()

    data class Error(
        val message: String,
        val action: (() -> Unit)? = null
    ) : Resource<Nothing>()
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
