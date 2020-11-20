package com.tedmob.africell.features.contactus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.R

class ContactUsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_contact_us, 0, false)
    }
}
