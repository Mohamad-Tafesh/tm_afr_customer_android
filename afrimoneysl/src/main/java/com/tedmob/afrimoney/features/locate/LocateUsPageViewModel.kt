package com.tedmob.afrimoney.features.locate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.entity.LocateUsEntry
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.features.locate.domain.GetLocateUsAgentsUseCase
import com.tedmob.afrimoney.features.locate.domain.GetLocateUsStoresUseCase
import com.tedmob.afrimoney.features.locate.domain.Params
import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocateUsPageViewModel
@Inject constructor(
    private val getLocateUsAgentsUseCase: GetLocateUsAgentsUseCase,
    private val getLocateUsStoresUseCase: GetLocateUsStoresUseCase,
    private val appExceptionFactory: AppExceptionFactory,
) : BaseViewModel() {

    val agents: LiveData<Resource<List<LocateUsEntry>>> get() = _agents
    private val _agents = MutableLiveData<Resource<List<LocateUsEntry>>>()

    val stores: LiveData<Resource<List<LocateUsEntry>>> get() = _stores
    private val _stores = MutableLiveData<Resource<List<LocateUsEntry>>>()


    fun getEntries(section: LocateUsViewModel.Section, latlong: MapLatLng) {
        when (section) {
            LocateUsViewModel.Section.Agents -> {
                executeResource(
                    getLocateUsAgentsUseCase,
                    Params(latlong.latitude, latlong.longitude),
                    _agents,
                    appExceptionFactory,
                    null,
                    action = { getEntries(section, latlong) },
                )
            }

            LocateUsViewModel.Section.Stores -> {
                /*executeResource(
                    getLocateUsStoresUseCase,
                    Unit,
                    _stores,
                    appExceptionFactory,
                    null,
                    action = { getEntries(section) },
                )*/
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}