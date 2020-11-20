package com.tedmob.africell.features.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.R

class HelpFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_help, 0, false)
    }
}
