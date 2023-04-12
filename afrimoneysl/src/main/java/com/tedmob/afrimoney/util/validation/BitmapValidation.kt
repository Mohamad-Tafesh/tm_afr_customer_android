package com.tedmob.afrimoney.util.validation

import android.graphics.Bitmap
import com.tedmob.libraries.validators.rules.Rule

class BitmapExistsRule(override val errorMessage: String) : Rule<Bitmap?> {
    override suspend fun isValid(value: Bitmap?): Boolean = value != null
}