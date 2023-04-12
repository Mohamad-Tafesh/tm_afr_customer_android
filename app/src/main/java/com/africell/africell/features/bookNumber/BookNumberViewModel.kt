package com.africell.africell.features.bookNumber

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.api.dto.StatusDTO

import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.bookNumber.domain.BookNumberUseCase
import com.africell.africell.features.bookNumber.domain.GetBookNumberUseCase

import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookNumberViewModel
@Inject constructor(
    private val getBookNumberUseCase: GetBookNumberUseCase,
    private  val bookNumberUseCase: BookNumberUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val freeNumbersData = MutableLiveData<Resource<List<String>>>()
    val bookNumberData = SingleLiveEvent<Resource<StatusDTO>>()

    fun getFreeNumbers(search:String?) {
        ResourceUseCaseExecutor(getBookNumberUseCase,GetBookNumberUseCase.Params(search), freeNumbersData,appExceptionFactory, appSessionNavigator) {
            getFreeNumbers(search)
        }.execute()
    }

    fun bookNumber(number:String?) {
        ResourceUseCaseExecutor(bookNumberUseCase,BookNumberUseCase.Params(number), bookNumberData,appExceptionFactory, appSessionNavigator) .execute()
    }

    override fun onCleared() {
        getBookNumberUseCase.dispose()
        super.onCleared()
    }
}
