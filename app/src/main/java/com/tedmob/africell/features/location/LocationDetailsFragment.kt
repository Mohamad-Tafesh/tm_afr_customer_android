package com.tedmob.africell.features.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.util.html.html
import com.tedmob.africell.util.intents.dial
import com.tedmob.africell.util.intents.email
import com.tedmob.africell.util.intents.openGoogleMapNavigation
import kotlinx.android.synthetic.main.fragment_location_details.*
import java.lang.IllegalArgumentException
import javax.inject.Inject


class LocationDetailsFragment : BaseFragment() {


    val location by lazy {
        arguments?.getParcelable<LocationDTO>(LOCATION_DETAILS) ?:   throw IllegalArgumentException("required Type arguments")
    }
    companion object{
        const val LOCATION_DETAILS="location_details"
    }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var mapFragment: SupportMapFragment? = null

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.location)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpMapConfiguration()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return activity?.let {
            return wrap(inflater.context, R.layout.fragment_location_details, 0, false)
        }
    }


    private fun setUpMapConfiguration() {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
        }

        mapFragment?.let { childFragmentManager.beginTransaction().replace(R.id.map, it).commit() }

        mapFragment?.getMapAsync { googleMap ->
            clearMap(googleMap)
            location?.latitude?.let { latitude ->
                location?.longitude?.let { longitude ->
                    val latLng = LatLng(latitude, longitude)

                    context?.let {
                        val marker =
                            googleMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.maps_icon_selected)))
                        marker.title = "${location?.title}"
                        marker.tag = location

                        fitBounds(googleMap, marker)
                    }
                }
                fillLocationInfo(location)
            }
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
        selectedLocation?.let { location ->
            title.text = location.title

            distance.text = location.displayDistance()
            description.text = location.description?.html()
         //   workingHrsValue.text= location.workingHrs?.joinToString(separator =", ")
            callUs.setOnClickListener {
                location.phoneNb?.let { dial(it) } ?: showMessage("Phone number is not available")
            }
            getDirection.setOnClickListener {
                if (location.latitude != null && location.longitude != null) {
                    openGoogleMapNavigation(location.latitude!!, location.longitude!!)
                } else {
                    showMessage(getString(R.string.direction_not_available))
                }
            }
        }
    }


}