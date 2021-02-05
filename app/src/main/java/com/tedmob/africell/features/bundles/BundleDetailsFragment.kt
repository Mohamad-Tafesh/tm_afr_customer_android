package com.tedmob.africell.features.bundles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.BundleInfo
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.activateBundle.ActivateBundleFragment.Companion.ACTIVATE_FOR_ME
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_bundle_details.*
import kotlinx.android.synthetic.main.row_bundle.view.*
import javax.inject.Inject


class BundleDetailsFragment : BaseFragment() {


    val bundle by lazy {
        arguments?.getParcelable<BundleInfo>(BUNDLE_DETAILS)
            ?: throw IllegalArgumentException("required bundle arguments")
    }

    companion object {
        const val BUNDLE_DETAILS = "bundle_details"
    }

@Inject lateinit var  sessionRepository: SessionRepository

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = bundle.getTitle()
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
            return wrap(inflater.context, R.layout.fragment_bundle_details, R.layout.toolbar_default, false)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        imageView.setImageURI(bundle.image)
        volumeTxt.text = bundle.getFormatVolume()
        validityTxt.text = bundle.getFormatValidity()
        descriptionTxt.text = bundle.commercialName
        priceTxt.text ="Price: "+ bundle.price
        validForTxt.text = "Valid for: "+ bundle.getFormatValidity()
        activateBundleForSomeOneElseBtn.setOnClickListener {
            if(sessionRepository.isLoggedIn()) {
                navigateToBundleActive(false)
            }else showLoginMessage()
        }
        activateBundleBtn.setOnClickListener {
            if(sessionRepository.isLoggedIn()) {
                navigateToBundleActive(true)
            }else showLoginMessage()
        }
    }

    private fun navigateToBundleActive(isActiveForMe:Boolean) {
        val bundle= bundleOf(Pair(BUNDLE_DETAILS,bundle),Pair(ACTIVATE_FOR_ME,isActiveForMe))
        findNavController().navigate(R.id.action_bundleDetailsFragment_to_activateBundleFragment,bundle)
    }


}