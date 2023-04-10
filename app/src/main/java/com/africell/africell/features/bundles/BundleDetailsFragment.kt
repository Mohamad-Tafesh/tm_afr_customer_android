package com.africell.africell.features.bundles

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.dto.BundleInfo
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentBundleDetailsBinding
import com.africell.africell.databinding.ToolbarDefaultBinding
import com.africell.africell.features.activateBundle.ActivateBundleFragment
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import javax.inject.Inject


class BundleDetailsFragment : BaseVBFragment<FragmentBundleDetailsBinding>() {


    val bundleId by lazy {
        arguments?.getString(BUNDLE_ID)
            ?: throw IllegalArgumentException("required bundle arguments")
    }

    val primaryColor by lazy {
        arguments?.getString(KEY_PRIMARY_COLOR_HEX)
    }

    val secondaryColor by lazy {
        arguments?.getString(KEY_SECONDARY_COLOR_HEX)
    }

    private val viewModel by provideViewModel<BundleDetailsViewModel> {
        viewModelFactory
    }

    companion object {
        const val BUNDLE_ID = "bundle_id"
        const val KEY_PRIMARY_COLOR_HEX = "primary_color"
        const val KEY_SECONDARY_COLOR_HEX = "secondary_color"
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
            return createViewBinding(
                container,
                FragmentBundleDetailsBinding::inflate,
                true,
                ToolbarDefaultBinding::inflate
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()
        primaryColor?.let { changeBackgroundColor(it) }
    }

    private fun bindData() {
        viewModel.getBundlesDetails(bundleId)
        observeResourceInline(viewModel.bundlesData, {
            setUpUI(it)
        })
    }

    private fun setUpUI(bundle: BundleInfo) {
        withVBAvailable {
            try {
                bundle.primaryColor?.let { changeBackgroundColor(it) }
                val secondaryColor = Color.parseColor(bundle.secondaryColor)
                volumeTxt.setTextColor(secondaryColor)
                subtitleTxt.setTextColor(secondaryColor)
                descriptionTxt.setTextColor(secondaryColor)
                priceTxt.setTextColor(secondaryColor)
                validForTxt.setTextColor(secondaryColor)
                activateBundleForSomeOneElseBtn.setBackgroundColor(secondaryColor)
                activateBundleBtn.setBackgroundColor(secondaryColor)
            } catch (e: Exception) {

            }
            imageView.setImageURI(bundle.image)
            actionbar?.title = bundle.getTitle()
            volumeTxt.text = bundle.getFormatVolume()
            subtitleTxt.text = bundle.subTitles
            descriptionTxt.text = bundle.commercialName
            priceTxt.text = getString(R.string.price) + bundle.price
            validForTxt.text = getString(R.string.valid_for) + bundle.validity + bundle.validityUnit
            activateBundleForSomeOneElseBtn.setOnClickListener {
                if (sessionRepository.isLoggedIn()) {
                    navigateToBundleActive(false, bundle)
                } else showLoginMessage()
            }
            activateBundleBtn.setOnClickListener {
                if (sessionRepository.isLoggedIn()) {
                    navigateToBundleActive(true, bundle)
                } else showLoginMessage()
            }
        }
    }

    private fun changeBackgroundColor(primaryColor: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                activity?.window?.statusBarColor = Color.parseColor(primaryColor)
            }
            toolbar?.setBackgroundColor(Color.parseColor(primaryColor))
            toolbar?.backgroundTintList = ColorStateList.valueOf(Color.parseColor(primaryColor))
        } catch (e: Exception) {

        }
    }

    private fun navigateToBundleActive(isActiveForMe: Boolean, bundle: BundleInfo) {
        //    val bundle= bundleOf(Pair(BUNDLE_DETAILS,bundle),Pair(ACTIVATE_FOR_ME,isActiveForMe))
        val bottomSheetFragment = ActivateBundleFragment.newInstance(bundle, isActiveForMe)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        //  findNavController().navigate(R.id.action_bundleDetailsFragment_to_activateBundleFragment,bundle)
    }


}