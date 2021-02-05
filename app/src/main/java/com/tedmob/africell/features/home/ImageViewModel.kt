package com.tedmob.africell.features.home

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.DefaultObserver
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.entity.SubAccount
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.exception.AppException
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.accountInfo.domain.GetSubAccountUseCase
import com.tedmob.africell.features.authentication.domain.SetUserPushUseCase
import com.tedmob.africell.features.home.domain.GetImagesUseCase
import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject


class ImageViewModel
@Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

   val imagesData = MutableLiveData<Resource<List<String>>>()
    fun getImages(imageType: String?, pageName: String?) {
        ResourceUseCaseExecutor(
            getImagesUseCase,
            GetImagesUseCase.Params(imageType, pageName),
            imagesData,
            appExceptionFactory,
            appSessionNavigator
        ).execute()
    }

    override fun onCleared() {
        super.onCleared()
        getImagesUseCase.dispose()

    }
}