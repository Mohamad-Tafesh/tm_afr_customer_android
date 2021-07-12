package com.africell.africell.features.help

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Help(
    @DrawableRes var image: Int,
    @StringRes var message: Int
                )
