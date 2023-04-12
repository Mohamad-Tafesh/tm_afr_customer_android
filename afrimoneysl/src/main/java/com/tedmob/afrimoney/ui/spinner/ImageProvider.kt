package com.tedmob.afrimoney.ui.spinner

import android.content.Context
import android.graphics.drawable.Drawable

interface ImageProvider {
    fun getImage(context: Context): Drawable?
}