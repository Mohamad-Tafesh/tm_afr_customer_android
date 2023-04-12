package com.tedmob.afrimoney.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BundlelistParent(
    val bundlelist: List<Bundlelist>,
    val displayName: String,
    val icon: String,
    val receiver_idType: String,
    val receiver_idValue: String,
): Parcelable
