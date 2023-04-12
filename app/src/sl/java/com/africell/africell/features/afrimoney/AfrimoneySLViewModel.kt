package com.africell.africell.features.afrimoney

import androidx.lifecycle.LiveData
import com.africell.africell.app.AppSessionNavigator
import com.tedmob.afrimoney.data.Resource
import com.africell.africell.data.SingleLiveEvent
import com.africell.africell.data.entity.afrimoney.HomeData
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.afrimoney.domain.GetHomeDataUseCase
import com.africell.africell.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AfrimoneySLViewModel
@Inject constructor(
    private val getHomeUseCase: GetHomeDataUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

    val data: LiveData<Resource<HomeData>> get() = _data
    private val _data = SingleLiveEvent<Resource<HomeData>>()


    fun getData() {
        executeResource(
            getHomeUseCase,
            Unit,
            _data,
            appExceptionFactory,
            appSessionNavigator,
            action = { getData() },
        )
    }
}