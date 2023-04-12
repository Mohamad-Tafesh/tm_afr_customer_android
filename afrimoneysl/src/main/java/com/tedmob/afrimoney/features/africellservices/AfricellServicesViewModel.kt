package com.tedmob.afrimoney.features.africellservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.app.handleInvalidSession
import com.tedmob.afrimoney.data.*
import com.tedmob.afrimoney.data.entity.BundlelistParent
import com.tedmob.afrimoney.data.entity.SubmitResult
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.africellservices.domain.GetAfricellServicesUseCase
import com.tedmob.afrimoney.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AfricellServicesViewModel
@Inject constructor(
    private val servicesUseCase: GetAfricellServicesUseCase,
    private val appExceptionFactory: AppExceptionFactory,
    private val session: SessionRepository,
    private val appSessionNavigator: AppSessionNavigator,

    ) : BaseViewModel() {

    data class YaRemix(
        val number: String,
        val amount: String,
    )


    val data: LiveData<Resource<List<BundlelistParent>>> get() = _data
    private val _data = MutableLiveData<Resource<List<BundlelistParent>>>()


    fun getServices() {

        execute(
            servicesUseCase,
            Unit,
            onLoading = {
                _data.emitLoading()
            },
            onSuccess = {
                _data.emitSuccess(it)
            },
            onError = {
                val exception = appExceptionFactory.make(it)
                if (!exception.handleInvalidSession(appSessionNavigator)) {
                    _data.emitError(exception.userMessage)
                }
            }
        )
    }


}