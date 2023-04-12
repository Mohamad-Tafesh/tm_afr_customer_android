package com.tedmob.afrimoney.features.locate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.entity.LocateUsEntry
import com.tedmob.afrimoney.databinding.FragmentLocateMapBinding
import com.tedmob.afrimoney.databinding.ItemLocateListOnMapBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.provideParentViewModel
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter
import com.tedmob.afrimoney.util.intents.dial
import com.tedmob.afrimoney.util.intents.openGoogleMap
import com.tedmob.modules.mapcontainer.MapFragment
import com.tedmob.modules.mapcontainer.view.MapHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocateMapFragment : BaseVBFragment<FragmentLocateMapBinding>() {


    companion object {
        private const val KEY__SECTION = "afrimoney.LocateMapFragment__section"

        fun newInstance(section: LocateUsViewModel.Section) = LocateMapFragment().apply {
            arguments = bundleOf(
                KEY__SECTION to section.ordinal,
            )
        }
    }

    var previousSelectedMarker: Marker? = null
    private var list: List<LocateUsEntry> = emptyList()
    private val parentViewModel by provideParentViewModel<LocateUsViewModel>()
    private val viewModel by provideViewModel<LocateUsPageViewModel>()
    private val section: LocateUsViewModel.Section by lazy {
        LocateUsViewModel.Section.values()[requireArguments().getInt(KEY__SECTION)]
    }

    private var isFirstTime: Boolean = true
    private var map: MapHolder? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentLocateMapBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = emptyList()
        isFirstTime = true

        bindData()

        viewModel.getEntries(section, parentViewModel.longlat)
    }

    private fun bindData() {
        val liveData = when (section) {
            LocateUsViewModel.Section.Agents -> viewModel.agents
            LocateUsViewModel.Section.Stores -> viewModel.stores
        }

        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    if (isFirstTime) {
                        showInlineLoading()
                    } else {
                        //do nothing
                    }
                }
                is Resource.Success -> {
                    if (isFirstTime) {
                        showContent()
                        withVBAvailable {
                            setupMap(it.data)
                            setUpAgents(it.data)
                            list = it.data
                        }
                    } else {
                        withVBAvailable {
                            setupItems(it.data)
                        }
                    }
                }
                is Resource.Error -> {
                    if (isFirstTime) {
                        if (it.action == null) {
                            showInlineMessage(it.message)
                        } else {
                            showInlineMessageWithAction(
                                it.message,
                                getString(R.string.retry),
                                it.action
                            )
                        }
                    } else {
                        if (it.action == null) {
                            showMaterialMessageDialog(it.message)
                        } else {
                            showMaterialMessageDialog(
                                it.message,
                                getString(R.string.retry),
                                callback = it.action
                            )
                        }
                    }
                }
            }
        }
    }

    private fun FragmentLocateMapBinding.setupMap(items: List<LocateUsEntry>) {
        previousSelectedMarker = null
        mapContainer.getFragment<MapFragment>()
            .getMapAsync {
                map = it

                //TODO location related

                it.setOnMapLoadedCallback {
                    setupItems(items)
                }
            }
    }

    private fun FragmentLocateMapBinding.setupItems(items: List<LocateUsEntry>) {
        map?.clear()

        items.forEach {
            map?.addMarker(it.coordinates) {
                title = it.title

                tag = it
            }
        }

        map?.setOnMarkerClickListener {
            binding?.showAsSelected(it.tag as LocateUsEntry)
            // it.setIcon("",BitmapDescriptorFactory.fromResource(R.mipmap.maps_icon_selected))
            false
        }
        map?.setOnMapClickListener {
            binding?.hideSelected()
            //setMarkerSelected(null)
        }
    }

    fun setMarkerSelected(marker: Marker?) {
        previousSelectedMarker?.let { it.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.maps_icon)) }
        marker?.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.maps_icon_selected))
        previousSelectedMarker = marker
    }

    private fun FragmentLocateMapBinding.hideSelected() {
        //bottomSection.isVisible = false
        locateUsRV.isVisible = false
    }

    private fun FragmentLocateMapBinding.showAsSelected(item: LocateUsEntry) {
        locateUsRV.isVisible = true
        var index = -1
        index = list.indexOfFirst { it.title == item.title }
        locateUsRV.smoothScrollToPosition(index)
        /*
                distanceText.text = item.distanceFormatted
                titleText.text = item.title
                addressText.text = item.address

                callUsText.setDebouncedOnClickListener { dial(item.phoneNumber) }
                directionsText.setDebouncedOnClickListener {
                    openGoogleMap(
                        item.coordinates.latitude,
                        item.coordinates.longitude,
                    )
                }*/

    }


    fun setUpAgents(item: List<LocateUsEntry>) {
        withVBAvailable {
            locateUsRV.adapter = adapter(item) {
                viewBinding(ItemLocateListOnMapBinding::inflate)
                onBindItemToViewBinding<ItemLocateListOnMapBinding> {
                    distanceText.text = it.distanceFormatted
                    titleText.text = it.title
                    addressText.text = it.address

                    callUsText.setDebouncedOnClickListener { click -> dial(it.phoneNumber) }
                    directionsText.setDebouncedOnClickListener { view ->
                        openGoogleMap(
                            it.coordinates.latitude,
                            it.coordinates.longitude,
                        )
                    }

                    root.setDebouncedOnClickListener { v ->

                    }
                }
            }
        }

    }


}