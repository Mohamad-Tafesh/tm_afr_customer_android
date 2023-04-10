package com.africell.africell.features.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.LocationDTO
import com.africell.africell.databinding.FragmentLocationMapBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.intents.dial
import com.africell.africell.util.intents.openGoogleMapNavigation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationMapFragment : BaseVBFragment<FragmentLocationMapBinding>() {
    /*    @Inject
        lateinit var currentLocation: CurrentLocation*/
    var googleMap: GoogleMap? = null

    var longitude: Double? = null
    var latitude: Double? = null

    private val viewModel by provideViewModel<LocationViewModel>()

    private var mapFragment: SupportMapFragment? = null

    //  private var currentlyShowingRider: RiderClusterItem? = null
    var previousSelectedMarker: Marker? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getUserLocationPermission()
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    private fun setupLayout() {
        setUpMapConfiguration()
        bindLocationsData()
    }

    private fun bindLocationsData() {
        observeResourceInline(viewModel.locationData) {
            setUpMap(it)
        }
    }


    fun setUpMap(locations: List<LocationDTO>) {
        val markers: MutableList<Marker> = mutableListOf<Marker>()
        googleMap?.let { googleMap ->
            locations.forEachIndexed { index, location ->

                location.latitude?.toDoubleOrNull()?.let { latitude ->
                    location.longitude?.toDoubleOrNull()?.let { longitude ->
                        val latLng = LatLng(latitude, longitude)
                        context?.let {
                            val marker = googleMap.addMarker(
                                MarkerOptions().position(latLng).icon(
                                    BitmapDescriptorFactory.fromResource(
                                        R.mipmap.maps_icon
                                    )
                                )
                            )
                            marker?.title = "${location.shopName}"
                            marker?.tag = location
                            if (marker != null) {
                                markers.add(marker)
                            }

                        }


                        googleMap.setOnMapClickListener {
                            withVBAvailable {
                                locationRootInfo.visibility = View.GONE
                                setMarkerSelected(null)
                            }
                        }

                        googleMap.setOnMarkerClickListener { marker ->
                            withVBAvailable {
                                locationRootInfo.visibility = View.VISIBLE
                                val location = marker.tag as? LocationDTO
                                location?.let {
                                    fillLocationInfo(location)
                                    setMarkerSelected(marker)
                                }
                            }
                            true
                        }

                    }
                }
            }
            fitBoundsForAllMarkers(googleMap, markers)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.let {
            createViewBinding(container, FragmentLocationMapBinding::inflate, true, ToolbarDefaultBinding::inflate)
        }
    }


    @SuppressLint("MissingPermission")
    private fun setUpMapConfiguration() {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
        }

        mapFragment?.let { childFragmentManager.beginTransaction().replace(R.id.map, it).commit() }


        mapFragment?.getMapAsync { googleMap ->
            this@LocationMapFragment.googleMap = googleMap
            clearMap(googleMap)
            displayUserLocation(googleMap)
        }
    }

    private fun clearMap(googleMap: GoogleMap) {
        googleMap.clear()
        previousSelectedMarker = null
    }

    private fun displayUserLocation(googleMap: GoogleMap) {
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
                if (task.isSuccessful) {
                    task.result?.longitude?.let { lng ->
                        task.result?.latitude?.let { lat ->
                            latitude = lat
                            longitude = lng
                            viewModel.getLocations(null, latitude, longitude)
                            /*context?.let {
                                //googleMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.user_location)))
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
                            }*/
                        }
                    } ?: viewModel.getLocations(null, null, null)

                } else {
                    viewModel.getLocations(null, null, null)
                }


            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }

    }


    fun fitBoundsForAllMarkers(googleMap: GoogleMap, markers: MutableList<Marker>) {
        googleMap.setOnMapLoadedCallback {
            if (markers.size > 0) {
                val builder = LatLngBounds.Builder()
                markers.forEach { marker ->
                    builder.include(marker.position)
                }
                val bounds = builder.build()
                val cu = CameraUpdateFactory.newLatLngBounds(bounds, 30)
                cu?.let {
                    googleMap.moveCamera(cu)
                }
            }
        }

    }

    fun setMarkerSelected(marker: Marker?) {
        previousSelectedMarker?.let { it.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.maps_icon)) }
        marker?.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.maps_icon_selected))
        previousSelectedMarker = marker
    }


    private fun fillLocationInfo(selectedLocation: LocationDTO?) {
        withVBAvailable {
            selectedLocation?.let { location ->
                title.text = location.shopName

                distance.text = location.displayDistance(latitude, longitude)
                description.text = location.address
                getDirection.setOnClickListener {
                    val lat = location.latitude?.toDoubleOrNull()
                    val long = location.longitude?.toDoubleOrNull()

                    if (lat != null && long != null) {
                        openGoogleMapNavigation(lat, long)
                    } else {
                        showMessage(getString(R.string.direction_not_available))
                    }
                }
                callUs.setOnClickListener {
                    if (!location.telephoneNumber.isNullOrEmpty()) {
                        dial(location.telephoneNumber)
                    } else showMessage(getString(R.string.phone_number_is_not_available))
                }
                locationRootInfo.setOnClickListener {
                    // viewModelLocationDetails.details.value=location
                    //   findNavController().navigate(R.id.action_locationMapFragment_to_locationDetailsFragment)

                }


            }
            locationRootInfo.visibility = View.VISIBLE
        }

    }


    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102
    private fun getUserLocationPermission() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupLayout()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupLayout()
                } else {
                    showInfoMessage(getString(R.string.to_server_you_better_please_enable_google_map))
                    setupLayout()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_location_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.navToListView) {
            findNavController().navigate(R.id.action_global_locationListFragment)
            true
        } else super.onOptionsItemSelected(item)

    }
}