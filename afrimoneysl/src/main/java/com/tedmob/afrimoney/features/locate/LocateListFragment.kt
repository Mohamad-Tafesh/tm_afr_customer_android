package com.tedmob.afrimoney.features.locate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.Resource
import com.tedmob.afrimoney.data.entity.LocateUsEntry
import com.tedmob.afrimoney.databinding.FragmentLocateListBinding
import com.tedmob.afrimoney.ui.viewmodel.provideParentViewModel
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.intents.dial
import com.tedmob.afrimoney.util.intents.openGoogleMapNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocateListFragment : BaseVBFragment<FragmentLocateListBinding>() {

    companion object {
        private const val KEY__SECTION = "afrimoney.LocateListFragment__section"

        fun newInstance(section: LocateUsViewModel.Section) = LocateListFragment().apply {
            arguments = bundleOf(
                KEY__SECTION to section.ordinal,
            )
        }
    }


    private val parentViewModel by provideParentViewModel<LocateUsViewModel>()
    private val viewModel by provideViewModel<LocateUsPageViewModel>()
    private val section: LocateUsViewModel.Section by lazy {
        LocateUsViewModel.Section.values()[requireArguments().getInt(KEY__SECTION)]
    }
    private var isFirstTime: Boolean = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentLocateListBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isFirstTime = true

        withVBAvailable {
            locateUsRV.addItemDecoration(
                MaterialDividerItemDecoration(locateUsRV.context, MaterialDividerItemDecoration.VERTICAL)
                    .apply {
                        setDividerColorResource(locateUsRV.context, R.color.greyHighlight)
                        setDividerInsetStartResource(locateUsRV.context, R.dimen.divider_inset_start)
                    }
            )
            locateUsRV.adapter = LocateUsListAdapter(
                onCallUsClick = { dial(it.phoneNumber) },
                onDirectionsClick = {
                    openGoogleMapNavigation(
                        it.coordinates.latitude,
                        it.coordinates.longitude,
                    )
                },
            )
        }

        bindData()

        viewModel.getEntries(section,parentViewModel.longlat)
    }

    private fun bindData() {
        val liveData = when (section) {
            LocateUsViewModel.Section.Agents -> viewModel.agents
            LocateUsViewModel.Section.Stores -> viewModel.stores
        }

        liveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    if (isFirstTime) {
                        showInlineLoading()
                    } else {
                        //do nothing
                    }
                }
                is Resource.Success -> {
                    if (isFirstTime) {
                        showContent()
                        withVBAvailable {
                            setupItems(it.data)
                        }
                    } else {
                        withVBAvailable {
                            setupItems(it.data)
                        }
                    }
                }
                is Resource.Error -> {
                    if (isFirstTime) {
                        if (it.action == null) {
                            showInlineMessage(it.message)
                        } else {
                            showInlineMessageWithAction(
                                it.message,
                                getString(R.string.retry),
                                it.action
                            )
                        }
                    } else {
                        if (it.action == null) {
                            showMaterialMessageDialog(it.message)
                        } else {
                            showMaterialMessageDialog(
                                it.message,
                                getString(R.string.retry),
                                callback = it.action
                            )
                        }
                    }
                }
            }
        }
    }


    private fun FragmentLocateListBinding.setupItems(items: List<LocateUsEntry>) {
        (locateUsRV.adapter as? LocateUsListAdapter?)?.submitList(items)
    }
}