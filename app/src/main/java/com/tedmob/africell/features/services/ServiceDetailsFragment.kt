package com.tedmob.africell.features.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_service_details.*
import javax.inject.Inject


class ServiceDetailsFragment : BaseFragment() {


    val service by lazy {
        arguments?.getParcelable<ServicesDTO>(SERVICE_DETAILS)
            ?: throw IllegalArgumentException("required Type arguments")
    }

    companion object {
        const val SERVICE_DETAILS = "service_details"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.let {
            return wrap(inflater.context, R.layout.fragment_service_details, R.layout.toolbar_default, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
     //   imageView.setImageURI(service.title)
    }


}