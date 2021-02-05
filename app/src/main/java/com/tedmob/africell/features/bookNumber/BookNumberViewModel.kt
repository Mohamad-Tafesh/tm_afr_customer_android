package com.tedmob.africell.features.bookNumber

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.SingleLiveEvent
import com.tedmob.africell.data.api.dto.StatusDTO

import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.bookNumber.domain.BookNumberUseCase
import com.tedmob.africell.features.bookNumber.domain.GetBookNumberUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

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
