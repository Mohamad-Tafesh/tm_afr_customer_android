package com.tedmob.africell.features.bundles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.BundleInfo
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.activateBundle.ActivateBundleFragment
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_bundle_details.*
import javax.inject.Inject


class BundleDetailsFragment : BaseFragment() {


    val bundleId by lazy {
        arguments?.getString(BUNDLE_ID)
            ?: throw IllegalArgumentException("required bundle arguments")
    }

    private val viewModel by provideViewModel<BundleDetailsViewModel> {
        viewModelFactory
    }

    companion object {
        const val BUNDLE_ID = "bundle_id"
    }

    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun configureToolbar() {
        super.configureToolbar()
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
            return wrap(inflater.context, R.layout.fragment_bundle_details, R.layout.toolbar_default, true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()
    }

    private fun bindData() {
        viewModel.getBundlesDetails(bundleId)
        observeResourceInline(viewModel.bundlesData,{
            setUpUI(it)
        })
    }

    private fun setUpUI(bundle:BundleInfo) {
        imageView.setImageURI(bundle.image)
        actionbar?.title = bundle.getTitle()
        volumeTxt.text = bundle.getFormatVolume()
        subtitleTxt.text = bundle.subTitles
        descriptionTxt.text = bundle.commercialName
        priceTxt.text = "Price: " + bundle.price
        validForTxt.text = "Valid for: " + bundle.validity + bundle.validityUnit
        activateBundleForSomeOneElseBtn.setOnClickListener {
            if (sessionRepository.isLoggedIn()) {
                navigateToBundleActive(false,bundle)
            } else showLoginMessage()
        }
        activateBundleBtn.setOnClickListener {
            if (sessionRepository.isLoggedIn()) {
                navigateToBundleActive(true,bundle)
            } else showLoginMessage()
        }
    }

    private fun navigateToBundleActive(isActiveForMe: Boolean, bundle: BundleInfo) {
        //    val bundle= bundleOf(Pair(BUNDLE_DETAILS,bundle),Pair(ACTIVATE_FOR_ME,isActiveForMe))
        val bottomSheetFragment = ActivateBundleFragment.newInstance(bundle, isActiveForMe)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        //  findNavController().navigate(R.id.action_bundleDetailsFragment_to_activateBundleFragment,bundle)
    }


}