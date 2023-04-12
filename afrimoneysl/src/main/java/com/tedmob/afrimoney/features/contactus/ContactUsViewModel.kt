package com.tedmob.afrimoney.features.contactus

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.contactus.domain.GetContactUsContentUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ContactUsViewModel
@Inject constructor(
    private val getContactUsContentUseCase: GetContactUsContentUseCase,
    private val appExceptionFactory: AppExceptionFactory,
) : BaseViewModel() {

    val content: LiveData<Resource<Any>> get() = _content
    private val _content = SingleLiveEvent<Resource<Any>>()

    fun getData() {
        executeResource(
            getContactUsContentUseCase,
            Unit,
            _content,
            appExceptionFactory,
            null,
            action = { getData() },
        )
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}