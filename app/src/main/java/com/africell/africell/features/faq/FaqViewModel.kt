package com.africell.africell.features.faq

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.faq.domain.GetFaqItemsUseCase
import com.africell.africell.ui.BaseViewModel
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