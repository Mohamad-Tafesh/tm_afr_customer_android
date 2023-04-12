package com.tedmob.afrimoney.features.locate

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayoutMediator
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.AppDataHolder
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentLocateUsBinding
import com.tedmob.afrimoney.ui.viewmodel.provideParentViewModel
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.foras.util.location.LocationFragmentHelper
import com.tedmob.foras.util.location.LocationHelper
import com.tedmob.modules.mapcontainer.view.MapLatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocateUsFragment : BaseVBFragment<FragmentLocateUsBinding>() {

    private val locationHelper: LocationFragmentHelper by lazy { initLocationHelper() }
    private val parentViewModel by provideViewModel<LocateUsViewModel>()
    private var state: Int = MAP


    companion object {
        val MAP = 0
        val LIST = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentLocateUsBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
        actionbar?.setBackgroundDrawable(resources.getDrawable(R.drawable.actionbar_bgd))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationHelper.start()


    }

    private fun toggle() {
        when (state) {
            MAP -> selectList()
            LIST -> selectMap()
        }
    }

    private fun selectMap() {
        childFragmentManager.commit {
            replace(
                R.id.frameLayout,
                LocateMapFragment.newInstance(LocateUsViewModel.Section.Agents),
                "map"
            )
        }
        state = MAP
    }

    private fun selectList() {
        childFragmentManager.commit {
            replace(
                R.id.frameLayout,
                LocateListFragment.newInstance(LocateUsViewModel.Section.Agents),
                "list"
            )
        }
        state = LIST
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_locate_us, menu)
        super.onCreateOptionsMenu(menu, inflater)
        if (state == LIST) menu.getItem(0).setIcon(R.drawable.locateusmap)
        else menu.getItem(0).setIcon(R.drawable.locateuslist)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {


            R.id.toggleMapListAction -> {
                if (state == LIST) item.setIcon(R.drawable.locateusmap)
                else item.setIcon(R.drawable.locateuslist)
                toggle()
                true
            }
            else -> {

                if (state == LIST) item.setIcon(R.drawable.locateusmap)
                else item.setIcon(R.drawable.locateuslist)
                super.onOptionsItemSelected(item)
            }

        }

    }

    private inline fun initLocationHelper(): LocationFragmentHelper =
        LocationFragmentHelper(this, requireActivity()).apply {
            pauseInBackground = true
            locationSettingsRequestCode = 1000
            permissionsRequestCode = 1001
            callback = object : LocationHelper.Callback {

                override fun onLocationChanged(location: Location?) {
                    locationHelper.stop()
                    parentViewModel.longlat = MapLatLng(location?.longitude!!, location.latitude)
                    selectMap()


                }

                override fun onConnectionFailed(exception: Exception) {
                    showMessage(getString(R.string.location_helper__connection_failed))
                }

                override fun onLocationSettingsUnavailable() {
                    showMessage(getString(R.string.location_helper__failed_gps_fetching))
                }

                override fun onPermissionDenied() {
                    showMessage(getString(R.string.location_helper__permission_denied))
                    AppDataHolder.hasAskedLocationPermissionAndRefused = true
                }

                override fun onLocationSettingsDenied() {
                    showMessage(getString(R.string.location_helper__location_settings_denied))
                    AppDataHolder.hasAskedLocationPermissionAndRefused = true
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        locationHelper.handleActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationHelper.stop()
    }
}