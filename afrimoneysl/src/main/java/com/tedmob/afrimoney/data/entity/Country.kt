package com.tedmob.afrimoney.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tedmob.afrimoney.ui.spinner.searchable.MaterialSearchableSpinnerItem

data class Country(
    @field:[Expose SerializedName("code")] val code: String,
    @field:[Expose SerializedName("name")] val name: String,
    @field:[Expose SerializedName("dial_code")] val phonecode: String
) : MaterialSearchableSpinnerItem()/*, ImageProvider*/ {

    companion object {
        const val LEBANON_CODE = "+961"
        const val DEFAULT_CODE = "+232"
    }


    override fun toString(): String = phonecode //also used for spinner's selected view

    override fun toDisplayString(): String = "$name ($phonecode)" //used for spinner's searchable dialog's items

    /*override fun getImage(context: Context): Drawable? =
        context.getDrawableByName("cf_${code.toLowerCase(Locale.ENGLISH)}")


    private inline fun Context.getDrawableByName(name: String): Drawable? =
        resources.getIdentifier(name, "drawable", applicationInfo.packageName)
            .takeIf { it != 0 }
            ?.let { ContextCompat.getDrawable(this, it) }*/
}