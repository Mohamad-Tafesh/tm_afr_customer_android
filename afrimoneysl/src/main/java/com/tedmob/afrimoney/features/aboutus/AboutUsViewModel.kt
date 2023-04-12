package com.tedmob.afrimoney.features.aboutus

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.entity.AboutData
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.aboutus.domain.GetAboutUsContentUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel
@Inject constructor(
    private val getAboutUsContentUseCase: GetAboutUsContentUseCase,
    private val appExceptionFactory: AppExceptionFactory,
) : BaseViewModel() {

    val content: LiveData<Resource<AboutData>> get() = _content
    private val _content = SingleLiveEvent<Resource<AboutData>>()


    fun getData() {
        executeResource(
            getAboutUsContentUseCase,
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