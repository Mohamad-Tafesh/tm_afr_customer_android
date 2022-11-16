package com.africell.africell.features.myBundlesAndServices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.dto.ServicesDTO
import com.africell.africell.util.removeTime
import kotlinx.android.synthetic.main.fragment_my_bundle_details.*


class MyBundleDetailsFragment : BaseFragment() {


    val bundle by lazy {
        arguments?.getParcelable<ServicesDTO>(BUNDLE_DETAILS)
            ?: throw IllegalArgumentException("required bundle arguments")
    }

    companion object {
        const val BUNDLE_DETAILS = "bundle_details"
    }


    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = bundle?.name
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return activity?.let {
            return wrap(inflater.context, R.layout.fragment_my_bundle_details, R.layout.toolbar_default, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }


    private fun setUpUI() {
        validityTxt.text = bundle.expiryDate.orEmpty().removeTime()
        activatedOnValue.text = bundle.activateDate.orEmpty()
        subtitle.text = bundle.name.orEmpty()
        titleTxt.text = bundle.subTitle.orEmpty()
        descriptionTxt.text = bundle.description.orEmpty()
        balanceTitle.text = bundle.currentValue.orEmpty() + "/" + bundle.maxValue.orEmpty()
        expiryDateTxt.text = getString(R.string.exp) + bundle.expiryDate.orEmpty().removeTime()
        progressBar.max = bundle.maxValue?.toDoubleOrNull()?.toInt() ?: 100
        progressBar.setProgress(bundle.currentValue?.toDoubleOrNull()?.toInt() ?: 0)


    }


}