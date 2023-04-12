package com.tedmob.afrimoney.features.account

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBBottomSheetFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.crashlytics.CrashlyticsHandler
import com.tedmob.afrimoney.databinding.FragmentSelectAddressFromMapBinding
import com.tedmob.afrimoney.util.location.AppLocationServices
import com.tedmob.modules.mapcontainer.MapFragment
import com.tedmob.modules.mapcontainer.view.MapHolder
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class SelectAddressFromMapFragment : BaseVBBottomSheetFragment<FragmentSelectAddressFromMapBinding>() {

    sealed class MapResult {
        class Success(
            val location: MapLatLng? = null,
            val address: Address? = null
        ) : MapResult()

        object Cancelled : MapResult()
    }


    companion object {
        const val LIVE_DATA__MAP_RESULT = "live_data__map_result"

        private var hasRequestedLocationPermission: Boolean = false
    }


    private val args by navArgs<SelectAddressFromMapFragmentArgs>()
    private var centerLatLng: MapLatLng? = null
    private var centerAddress: Address? = null
    private val geocoder: Geocoder by lazy { Geocoder(requireActivity()) }
    private var fetchAddressJob: Job? = null
    private var mapFragment: MapFragment? = null
    private var map: MapHolder? = null
    private var movedCamera: Boolean = false

    @Inject
    lateinit var crashlytics: CrashlyticsHandler

    private val locationServices by lazy { AppLocationServices[requireActivity()] }

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ::handleLocationPermission)
    private val gpsLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult(), ::handleGPSSensorResult)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
            .let { dialog ->
                dialog.setOnShowListener {
                    val bottomSheet = dialog
                        .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

                    if (bottomSheet != null) {
                        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                        behavior.isDraggable = false
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
                dialog
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentSelectAddressFromMapBinding::inflate, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movedCamera = false

        withVBAvailable {
            addressText.isSelected = true

            setupMap()

            closeButton.setOnClickListener {
                _resultLiveData?.value = MapResult.Cancelled
                dismiss()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        _resultLiveData?.value = MapResult.Cancelled
    }


    private fun FragmentSelectAddressFromMapBinding.setupMap() {
        mapFragment = MapFragment.newInstance()

        childFragmentManager.commit(true) {
            replace(R.id.mapFragmentContainer, mapFragment!!)
        }

        mapFragment?.getMapAsync { mapHolder ->
            map = mapHolder

            //force check to see if activity is still alive.
            activity ?: return@getMapAsync
            //---

            moveWhenStartingCoordinatesFound()
            requestLocationPermissionsIfNeeded()

            map?.setOnMapLoadedCallback {
                pinImage.isVisible = true
                map?.attachCenterListener()
                moveLocationButton()

                selectButton.setOnClickListener {
                    _resultLiveData?.value = MapResult.Success(
                        centerLatLng,
                        centerAddress
                    )
                    dismiss()
                }


                centerLatLng = map?.getCameraPosition()?.target
                selectButton.isEnabled = false
                fetchAddressFrom(centerLatLng)
            }
        }
    }

    private fun moveWhenStartingCoordinatesFound() {
        args.startingLatLng?.let {
            if (!movedCamera) {
                map?.moveCamera { newLatLngZoom(it, 13f) }
            }
            movedCamera = true
        }
    }

    private fun requestLocationPermissionsIfNeeded() {
        if (
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestGPSSensor()
        } else if (!hasRequestedLocationPermission) {
            requestLocationPermission()
            hasRequestedLocationPermission = true
        }
    }

    private fun moveLocationButton() {
        mapFragment?.view?.findViewById<View>(Integer.parseInt("1"))
            ?.let { it.parent as? View }
            ?.findViewById<View>(Integer.parseInt("2"))
            ?.let {
                it.updateLayoutParams<RelativeLayout.LayoutParams> {
                    addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
                    addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                    setMargins(0, 180, 180, 0)
                }
            }
    }

    private fun MapHolder.attachCenterListener() {
        setOnCameraIdleListener {
            centerLatLng = getCameraPosition().target
            binding?.selectButton?.isEnabled = false
            fetchAddressFrom(centerLatLng)
        }
    }

    private fun fetchAddressFrom(center: MapLatLng?) {
        fetchAddressJob?.cancel()
        fetchAddressJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            if (center != null) {
                var addressLoaderJob: Job? = null

                runCatching {
                    binding?.selectButton?.isEnabled = false
                    addressLoaderJob = showAddressLoader()

                    val address = getAddressFromGeocoder(center)

                    addressLoaderJob?.cancel()
                    setAddress(address)
                    binding?.selectButton?.isEnabled = true
                }.onFailure {
                    if (it is CancellationException) throw it

                    addressLoaderJob?.cancel()
                    setAddress(null)
                    binding?.selectButton?.isEnabled = true
                    showMessage(it.localizedMessage.orEmpty())
                    crashlytics.log("Geocoder failed to get address information")
                    crashlytics.recordException(it)
                }
            } else {
                setAddress(null)
            }
        }
    }

    private fun CoroutineScope.showAddressLoader() = launch(Dispatchers.Main) {
        var count = 0

        while (isActive) {
            binding?.addressText?.setText(
                when (count) {
                    0 -> R.string.loading_1
                    1 -> R.string.loading_2
                    2 -> R.string.loading_3
                    else -> R.string.loading_3
                }
            )
            count++

            if (count == 3) {
                count = 0
            }

            delay(500L)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getAddressFromGeocoder(location: MapLatLng): Address? =
        withContext(Dispatchers.IO) {
            try {
                /*if (!Geocoder.isPresent()) {
                    throw IOException("Geocoder: isPresent: got RemoteException")
                }*/
                val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                list?.firstOrNull()
            } catch (e: Exception) {
                crashlytics.log("Geocoder failed to get address information")
                crashlytics.recordException(e)
                if (e is IOException) {
                    throw IOException(getString(R.string.error_network), e)
                } else {
                    throw RuntimeException(e)
                }
            }
        }

    private fun setAddress(address: Address?) {
        this.centerAddress = address
        binding?.addressText?.text = address?.getAddressLine(0)
    }


    private fun requestLocationPermission() {
        //force check to see if activity is still alive.
        activity ?: return
        //---

        permissionsLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun handleLocationPermission(results: Map<String, Boolean>) {
        if (results.any { it.value }) {
            requestGPSSensor()
        }
    }

    private fun requestGPSSensor() {
        //force check to see if activity is still alive.
        activity ?: return
        //---

        locationServices.getSettingsClient()
            .checkLocationSettings()
            .addOnSuccess {
                //force check to see if activity is still alive.
                activity ?: return@addOnSuccess
                //---

                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    map?.isMyLocationEnabled = true
                    map?.uiSettings?.isMyLocationButtonEnabled = true

                    centerToMyLocation()
                }
            }.addOnFailure { e ->
                when (e) {
                    is AppLocationServices.ResolvableApiException -> {
                        gpsLauncher.launch(
                            IntentSenderRequest.Builder(e.intentSender)
                                .build()
                        )
                    }
                    is AppLocationServices.SettingsUnavailableException -> {
                    }
                    is AppLocationServices.OtherApiException -> {
                    }
                    null -> {
                    }
                }
            }
    }

    private fun handleGPSSensorResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            //force check to see if activity is still alive.
            activity ?: return
            //---

            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true

                centerToMyLocation()
            }
        }
    }


    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun centerToMyLocation() {
        //force check to see if activity is still alive.
        activity ?: return
        //---

        if (!movedCamera) {
            locationServices.getFusedLocationProviderClient()
                .getLastLocation()
                .addOnSuccess {
                    if (it != null) {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        val coordinate = MapLatLng(latitude, longitude)
                        map?.animateCamera { newLatLngZoom(coordinate, 13f) }
                    }
                }

            movedCamera = true
        }
    }


    private inline val _resultLiveData: MutableLiveData<MapResult>?
        get() = findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.getLiveData(LIVE_DATA__MAP_RESULT)
}