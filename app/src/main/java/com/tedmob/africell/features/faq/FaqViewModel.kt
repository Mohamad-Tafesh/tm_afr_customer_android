package com.tedmob.africell.features.faq

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.faq.domain.GetFaqItemsUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class FaqViewModel
@Inject constructor(
    private val getFaqItems: GetFaqItemsUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val items: MutableLiveData<Resource<List<FaqItem>>> = MutableLiveData()


    fun getItems() {
        ResourceUseCaseExecutor(
            getFaqItems,
            Unit,
            items,
            appExceptionFactory,
            appSessionNavigator,
            { getItems() }
        ).execute()
    }


    override fun onCleared() {
        super.onCleared()
        getFaqItems.dispose()
    }
}