package com.africell.africell.ui.image

import com.facebook.drawee.view.SimpleDraweeView

inline fun SimpleDraweeView.setImageUriWithDimens(image: String?, width: Double?, height: Double?) {
    if (image != null) {
        if (width != null && height != null) {
            this.aspectRatio = (width / height).toFloat()
        }

        setImageURI(image)
    } else {
        setImageURI(null as String?)
    }
}