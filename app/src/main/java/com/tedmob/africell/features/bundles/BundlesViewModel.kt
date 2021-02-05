package com.tedmob.africell.features.bundles

import androidx.lifecycle.MutableLiveData
import com.tedmob.africell.app.AppSessionNavigator
import com.tedmob.africell.app.ResourceUseCaseExecutor
import com.tedmob.africell.data.Resource

import com.tedmob.africell.data.api.dto.BundleCategoriesDTO
import com.tedmob.africell.data.api.dto.BundlesDTO
import com.tedmob.africell.exception.AppExceptionFactory
import com.tedmob.africell.features.bundles.domain.GetBundleCategoriesUseCase
import com.tedmob.africell.features.bundles.domain.GetBundlesByCategoryUseCase

import com.tedmob.africell.ui.BaseViewModel
import javax.inject.Inject

class BundlesViewModel
@Inject constructor(
    private val getBundlesCategoryUseCase: GetBundleCategoriesUseCase,
    private val getBundlesByCategoryUseCase:GetBundlesByCategoryUseCase,
        private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator
) : BaseViewModel() {

    val bundleCategoriesData = MutableLiveData<Resource<List<BundleCategoriesDTO>>>()
  val bundlesData = MutableLiveData<Resource<List<BundlesDTO>>>()

    fun getBundlesCategories() {
        ResourceUseCaseExecutor(getBundlesCategoryUseCase,Unit, bundleCategoriesData,appExceptionFactory, appSessionNavigator) {
            getBundlesCategories()
        }.execute()
    }
    fun getBundlesByCategory(categoryId:Long?,search:String?) {
        getBundlesByCategoryUseCase.clear()
        ResourceUseCaseExecutor(getBundlesByCategoryUseCase,GetBundlesByCategoryUseCase.Params(categoryId,search), bundlesData,appExceptionFactory, appSessionNavigator) {
            getBundlesByCategory(categoryId,search)
        }.execute()
    }
    override fun onCleared() {
        getBundlesCategoryUseCase.dispose()
        getBundlesByCategoryUseCase.dispose()
        super.onCleared()
    }
}
