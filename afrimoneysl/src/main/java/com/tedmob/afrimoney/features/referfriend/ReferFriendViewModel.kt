package com.tedmob.afrimoney.features.referfriend

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.referfriend.domain.GetReferFriendContentUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReferFriendViewModel
@Inject constructor(
    private val getReferFriendContentUseCase: GetReferFriendContentUseCase,
    private val appExceptionFactory: AppExceptionFactory,
) : BaseViewModel() {

    val content: LiveData<Resource<Any>> get() = _content
    private val _content = SingleLiveEvent<Resource<Any>>()

    fun getData() {
        executeResource(
            getReferFriendContentUseCase,
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