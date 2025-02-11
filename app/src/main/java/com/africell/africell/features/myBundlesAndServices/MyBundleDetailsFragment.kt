package com.africell.africell.features.myBundlesAndServices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.ServicesDTO
import com.africell.africell.databinding.FragmentMyBundleDetailsBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.util.removeTime
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyBundleDetailsFragment : BaseVBFragment<FragmentMyBundleDetailsBinding>() {


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
            return createViewBinding(
                container,
                FragmentMyBundleDetailsBinding::inflate,
                false,
                ToolbarDefaultBinding::inflate
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }


    private fun setUpUI() {
        withVBAvailable {
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


}