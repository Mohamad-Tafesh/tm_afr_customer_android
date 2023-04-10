package com.africell.africell.features.home

import androidx.lifecycle.MutableLiveData
import com.africell.africell.app.AppSessionNavigator
import com.africell.africell.app.ResourceUseCaseExecutor
import com.africell.africell.data.Resource
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.home.domain.GetImagesUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
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