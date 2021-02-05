package com.tedmob.africell.features.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_location_map.*
import javax.inject.Inject


class LocationMapFragment : BaseFragment() {
    /*    @Inject
        lateinit var currentLocation: CurrentLocation*/
    var googleMap: GoogleMap? = null


    private val viewModel by provideViewModel<LocationViewModel> { viewModelFactory }

    private var mapFragment: SupportMapFragment? = null

    //  private var currentlyShowingRider: RiderClusterItem? = null
    var previousSelectedMarker: Marker? = null


    companion object {
        var viewLayout: View? = null
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getUserLocationPermission()
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
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
                            marker.title = "${location.shopName}"
                            marker.tag = location
                            markers.add(marker)

                        }


                        googleMap?.setOnMapClickListener {
                            locationRootInfo.visibility = View.GONE
                            setMarkerSelected(null)
                        }

                        googleMap?.setOnMarkerClickListener { marker ->
                            locationRootInfo.visibility = View.VISIBLE
                            val location = marker.tag as? LocationDTO
                            location?.let {
                                fillLocationInfo(location)
                                setMarkerSelected(marker)
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
            viewLayout = wrap(inflater.context, R.layout.fragment_location_map, R.layout.toolbar_default, true)
            viewLayout

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
                    task.result?.longitude?.let { longitude ->
                        task.result?.latitude?.let { latitude ->
                            val latLng = LatLng(latitude, longitude)
                            viewModel.getLocations(null,  task.result?.latitude, task.result?.longitude)
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
                cu?.let{
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
        selectedLocation?.let { location ->
            title.text = location.shopName

            distance.text = location.displayDistance()
            description.text = location.shopOwner
            /*  getDirection.setOnClickListener {
                  if (location.lat != null && location.lng != null) {
                      openGoogleMapNavigation(location.lat, location.lng)
                  } else {
                      showMessage(getString(R.string.direction_not_available))
                  }
              }
              callUs.setOnClickListener {
                  location.phoneNb?.let { dial(it) } ?: showMessage("Phone number is not available")
              }
              email.setOnClickListener {
                  location.email?.let{email(it)} ?: showMessage("Email is not available")
              }*/
            locationRootInfo.setOnClickListener {
                // viewModelLocationDetails.details.value=location
                //   findNavController().navigate(R.id.action_locationMapFragment_to_locationDetailsFragment)

            }


        }
        locationRootInfo.visibility = View.VISIBLE

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
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupLayout()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
                    showMessage("To serve you better please enable googleMap")
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