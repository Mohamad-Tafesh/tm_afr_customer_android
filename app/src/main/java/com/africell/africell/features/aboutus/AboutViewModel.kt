package com.africell.africell.features.aboutus
import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.data.api.dto.AboutDTO
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.aboutus.domain.AboutUsUseCase
import com.africell.africell.ui.BaseViewModel

import javax.inject.Inject


class AboutViewModel
@Inject constructor(
    private val aboutUsUseCase: AboutUsUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {


    val aboutData = MutableLiveData<Resource<AboutDTO>>()

    fun aboutUs() {
        ResourceUseCaseExecutor(aboutUsUseCase, Unit, aboutData,appExceptionFactory,appSessionNavigator){aboutUs()}.execute()
    }


    override fun onCleared() {
        super.onCleared()
        aboutUsUseCase.dispose()
    }
}