package com.tedmob.afrimoney.features.locate

import androidx.lifecycle.LiveData
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.exception.AppExceptionFactory
import com.tedmob.afrimoney.ui.BaseViewModel
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocateUsViewModel
@Inject constructor(
    //...
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) : BaseViewModel() {

     lateinit var longlat: MapLatLng

    enum class Mode {
        Map,
        List,
    }

    enum class Section {
        Agents,
        Stores,
    }


    val mode: LiveData<Mode> get() = _mode
    private val _mode = SingleLiveEvent<Mode>(Mode.Map)

    val currentMode: Mode get() = _mode.value!!

    //TODO save and send user location here


    fun toggleMode() {
        _mode.value = when (currentMode) {
            Mode.Map -> Mode.List
            Mode.List -> Mode.Map
        }
    }


    override fun onCleared() {
        super.onCleared()
        //...
    }
}