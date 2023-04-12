package com.tedmob.afrimoney.features.termsofuse

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.TermsData
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.termsofuse.domain.GetTermsOfUseContentUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TermsOfUseViewModel
@Inject constructor(
    private val getTermsOfUseContentUseCase: GetTermsOfUseContentUseCase,
    private val appExceptionFactory: AppExceptionFactory,
) : BaseViewModel() {

    val content: LiveData<Resource<TermsData>> get() = _content
    private val _content = SingleLiveEvent<Resource<TermsData>>()

    fun getData() {
        executeResource(
            getTermsOfUseContentUseCase,
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