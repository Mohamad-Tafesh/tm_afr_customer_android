package com.tedmob.africell.features.aboutus
import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.AboutDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.aboutus.domain.AboutUsUseCase
import com.tedmob.africell.ui.BaseViewModel

import javax.inject.Inject


class AboutViewModel
@Inject constructor(
    private val aboutUsUseCase: AboutUsUseCase,
    private val appExceptionFactory: AppExceptionFactory
) : BaseViewModel() {


    val aboutData = MutableLiveData<Resource<AboutDTO>>()

    fun aboutUs() {
        ResourceUseCaseExecutor(aboutUsUseCase, Unit, aboutData, appExceptionFactory){aboutUs()}.execute()
    }


    override fun onCleared() {
        super.onCleared()
        aboutUsUseCase.dispose()
    }
}