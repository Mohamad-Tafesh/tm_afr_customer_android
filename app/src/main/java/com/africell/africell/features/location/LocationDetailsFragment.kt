package com.africell.africell.features.location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.LocationDTO
import com.africell.africell.databinding.FragmentLocationDetailsBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.html.html
import com.africell.africell.util.intents.dial
import com.africell.africell.util.intents.openGoogleMapNavigation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationDetailsFragment : BaseVBFragment<FragmentLocationDetailsBinding>() {

    private val viewModel by provideViewModel<LocationViewModel>()
    val locationParams by lazy {
        arguments?.getParcelable<LocationDTO>(LOCATION_DETAILS)
    }
    val locationId by lazy {
        arguments?.getString(LOCATION_ID)
    }

    companion object {
        const val LOCATION_DETAILS = "location_details"
        const val LOCATION_ID = "location_id"
    }


    private var mapFragment: SupportMapFragment? = null

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.location)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()

    }

    private fun bindData() {
        viewModel.getLocationDetails(locationId, locationParams)
        observeResourceInline(viewModel.locationdetailsData, {
            setUpMapConfiguration(it)
            fillLocationInfo(it)

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.let {
            return createViewBinding(
                container,
                FragmentLocationDetailsBinding::inflate,
                true,
                ToolbarDefaultBinding::inflate
            )
        }
    }


    private fun setUpMapConfiguration(location: LocationDTO) {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
        }

        mapFragment?.let { childFragmentManager.beginTransaction().replace(R.id.map, it).commit() }

        mapFragment?.getMapAsync { googleMap ->
            clearMap(googleMap)
            location?.latitude?.toDoubleOrNull()?.let { latitude ->
                location?.longitude?.toDoubleOrNull()?.let { longitude ->
                    val latLng = LatLng(latitude, longitude)

                    context?.let {
                        val marker =
                            googleMap.addMarker(
                                MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.maps_icon_selected))
                            )
                        marker?.title = "${location.shopName}"
                        marker?.tag = location
                        getUserLocation(googleMap, location)
                        fitBounds(googleMap, marker)
                    }
                }

            }
        }

    }

    private fun getUserLocation(googleMap: GoogleMap, location: LocationDTO) {
        try {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.orEmpty())
        }
        try {
            val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            val locationResult = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                withVBAvailable {
                    if (task.isSuccessful) {
                        distance.text = location.displayDistance(task.result?.latitude, task.result?.longitude)
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }

    }


    private fun clearMap(googleMap: GoogleMap) {
        googleMap.clear()
    }


    fun fitBounds(googleMap: GoogleMap, marker: Marker?) {
        if (marker != null) {
            val builder = LatLngBounds.Builder()

            builder.include(marker.position)

            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 30)
            googleMap.moveCamera(cu)
        }
    }


    private fun fillLocationInfo(selectedLocation: LocationDTO?) {
        withVBAvailable {
            selectedLocation?.let { location ->
                title.text = location.shopName
                addressTxt.text = location.address

                description.text = location.description?.html()
                callUs.setOnClickListener {
                    if (!location.telephoneNumber.isNullOrEmpty()) {
                        dial(location.telephoneNumber)
                        //promptCallUs(location.numbers())
                    } else showMessage(getString(R.string.phone_number_is_not_available))

                }
                getDirection.setOnClickListener {
                    if (location.latitude?.toDoubleOrNull() != null && location.longitude?.toDoubleOrNull() != null) {
                        openGoogleMapNavigation(
                            location.latitude?.toDoubleOrNull()!!,
                            location.longitude?.toDoubleOrNull()!!
                        )
                    } else {
                        showMessage(getString(R.string.direction_not_available))
                    }
                }
            }
        }
    }


}