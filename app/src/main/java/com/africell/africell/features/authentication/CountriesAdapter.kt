package com.africell.africell.features.authentication

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.africell.africell.R
import com.africell.africell.data.entity.Country


class CountriesAdapter(context: Context, countries: List<Country>) :
    ArrayAdapter<Country>(context, R.layout.support_simple_spinner_dropdown_item, ArrayList(countries)) {

    init {
        setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        val country = getItem(position)
        view.text = "${country?.phonecode}"
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        val country = getItem(position)
        //view.text = country.phoneCode
        val context = view.context
        //    val flagRes = context.resources.getIdentifier("ic_country_flag_${country.code.toLowerCase()}", "mipmap", context.packageName)
        //TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(view, flagRes, 0, 0, 0)
        view.text = "${country?.name} (${country?.phonecode})"
        return view
    }


}