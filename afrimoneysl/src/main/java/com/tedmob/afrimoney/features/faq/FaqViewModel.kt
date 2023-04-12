package com.tedmob.afrimoney.features.faq

import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.faq.domain.GetFaqItemsUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FaqViewModel
@Inject constructor(
    private val getFaqItems: GetFaqItemsUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {

    val items: MutableLiveData<Resource<List<FaqItem>>> = MutableLiveData()


    fun getItems() {
        executeResource(
            getFaqItems,
            Unit,
            items,
            appExceptionFactory,
            null,
            { getItems() }
        )
    }
}