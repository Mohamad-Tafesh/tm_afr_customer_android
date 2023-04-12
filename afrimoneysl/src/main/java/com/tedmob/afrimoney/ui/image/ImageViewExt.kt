package com.tedmob.afrimoney.ui.image

import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import coil.request.ImageRequest
import com.tedmob.afrimoney.ui.awaitLayout
import kotlin.math.roundToInt

inline fun ImageRequest.Builder.optionalSize(width: Int?, height: Int?) {
    if (width != null && height != null) {
        size(width, height)
    }
}

suspend inline fun ImageView.setAspectRatio(aspectRatio: Double) {
    awaitLayout()

    when {
        layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT -> {
            updateLayoutParams {
                height = (width / aspectRatio).roundToInt()
            }
        }

        layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT -> {
            updateLayoutParams {
                width = (height * aspectRatio).roundToInt()
            }
        }
    }
}