package com.tedmob.africell.features.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.Resource
import com.tedmob.africell.data.api.dto.LocationDTO
import com.tedmob.africell.features.location.LocationDetailsFragment.Companion.LOCATION_DETAILS
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observe
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.getText
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class LocationFragment : BaseFragment() {
    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 102


    var longitude: Double? = null
    var latitude: Double? = null
    private val viewModel by provideViewModel<LocationViewModel> { viewModelFactory }

    val adapter by lazy {
        LocationAdapter(mutableListOf(), object : LocationAdapter.Callback {
            override fun onItemClickListener(item: LocationDTO) {
                val bundle = bundleOf(Pair(LOCATION_DETAILS, item))
                findNavController().navigate(R.id.action_locationListFragment_to_locationDetailsFragment, bundle)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_location, R.layout.toolbar_default, false)
    }

    private fun searchRxTextView() {
        searchTxt?.let {
            RxTextView.textChanges(it)
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ charSequence ->
                    viewModel.getLocations(charSequence.toString(),  latitude, longitude)
                }) { t -> }
        }
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = getString(R.string.location)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        searchRxTextView()
        getUserLocationPermission()
        bindData()
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator)
        drawable?.let {
            dividerItemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }
    }

    private fun bindData() {
        observe(viewModel.locationData) { res ->
            when (res) {
                is Resource.Loading -> {
                    swipeRefresh.isRefreshing = true
                }
                is Resource.Error -> {
                    showContent()
                    showMessageWithAction(res.message, getString(R.string.retry), res.action)
                    swipeRefresh.isRefreshing = false
                }
                is Resource.Success -> {
                    showContent()
                    val data = res.data
                    if (res.data.isNullOrEmpty()) {
                        emptyMessage.text = getString(R.string.no_location_available)
                    } else {
                        emptyMessage.text = ""
                    }
                    adapter.setItems(data)
                    swipeRefresh.isRefreshing = false
                }
                else -> {
                    swipeRefresh.isRefreshing = false
                }
            }


        }
    }

    private fun setupLayout() {
        getUserLocation()
    }


    private fun getUserLocation() {
        try {
            val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            val locationResult = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    longitude = task.result?.longitude
                    latitude = task.result?.latitude
                }
                viewModel.getLocations(searchTextLayout.getText(), latitude, longitude)


            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


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
        inflater.inflate(R.menu.menu_location_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.navToMapView) {
            findNavController().navigate(R.id.action_global_locationMapFragment)
            true
        } else super.onOptionsItemSelected(item)

    }
}

