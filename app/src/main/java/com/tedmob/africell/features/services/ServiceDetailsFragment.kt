package com.tedmob.africell.features.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.ServicesDTO
import com.tedmob.africell.ui.viewmodel.observeResource
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel

import kotlinx.android.synthetic.main.fragment_service_details.*


class ServiceDetailsFragment : BaseFragment() {

    val serviceParam by lazy {
        arguments?.getParcelable<ServicesDTO>(SERVICE_DETAILS)
    }

    val sname by lazy {
        arguments?.getString(KEY_SNAME)
    }

    companion object {
        const val KEY_SNAME = "sname"
        const val SERVICE_DETAILS = "service_details"
    }

    private val viewModel by provideViewModel<ServicesViewModel> { viewModelFactory }


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
            return wrap(inflater.context, R.layout.fragment_service_details, R.layout.toolbar_default, true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()
    }

    private fun bindData() {
        viewModel.getServiceDetails(sname,serviceParam)
        observeResourceInline(viewModel.serviceDetailsData, {
            setUpUI(it)
        })
    }

    private fun setUpUI(service: ServicesDTO) {
        imageView.setImageURI(service.image)
        volumeTxt.text = service.name.orEmpty() + "/" + service.validity.orEmpty()
        validityTxt.text = "Valid for a " + service.validity.orEmpty()
        descriptionTxt.text = service.description.orEmpty()
        priceTxt.text = "Price: " + service.price.orEmpty() + service.priceUnit.orEmpty()
        subtitleTxt.text = service.name.orEmpty()

        subscribeBtn.setOnClickListener {
            service.sname?.let { viewModel.subscribe(it) }

        }
        unsubscribeBtn.setOnClickListener {
            service.sname?.let {
                viewModel.unsubscribe(it)
            }
        }
        subscribeBtn.visibility = if (service.isActive == false) View.VISIBLE else View.GONE
        unsubscribeBtn.visibility =
            if (service.isActive == true && service.canUnsbscribe == true) View.VISIBLE else View.GONE
        unsubscribeBtn.setText(service.buttonLabel ?: "Unsubscribe")
        observeResource(viewModel.subscribeData) {
            unsubscribeBtn.visibility = View.GONE
            subscribeBtn.visibility = View.GONE
        }

        observeResource(viewModel.unSubscribeData) {
            unsubscribeBtn.visibility = View.GONE
            subscribeBtn.visibility = View.GONE
        }
        //   imageView.setImageURI(service.title)
    }


}