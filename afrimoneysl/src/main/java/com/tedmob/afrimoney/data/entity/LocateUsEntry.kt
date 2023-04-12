package com.tedmob.afrimoney.data.entity

import com.tedmob.modules.mapcontainer.view.MapLatLng

data class LocateUsEntry(
    val coordinates: MapLatLng,
    val distanceFormatted: String,
    val title: String?,
    val address: String?,
    val phoneNumber: String,
)