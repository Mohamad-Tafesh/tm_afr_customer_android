package com.africell.africell.features.myBundlesAndServices

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.api.dto.MyBundlesAndServices
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.databinding.FragmentMyBundlesServicesVpBinding
import com.africell.africell.databinding.ToolbarBundleServicesBinding
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MyBundleServicesVPFragment : BaseVBFragment<FragmentMyBundlesServicesVpBinding>() {

    @Inject
    lateinit var sessionRepository: SessionRepository


    private val viewModel by provideViewModel<MyBundlesAndServicesViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(
            container,
            FragmentMyBundlesServicesVpBinding::inflate,
            true,
            ToolbarBundleServicesBinding::inflate
        )
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.show()
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        actionbar?.title = ""
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getToolbarBindingAs<ToolbarBundleServicesBinding>()?.run {
            setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.BUNDLES_SERVICES)
        }
        bindData()

    }

    private fun bindData() {
        viewModel.getMyBundlesService()
        observeResourceInline(viewModel.myBundlesAndServicesData) { bundles ->
            setupViewPager(bundles)
        }
    }

    private fun setupViewPager(bundles: List<MyBundlesAndServices>) {

        withVBAvailable {
            getToolbarBindingAs<ToolbarBundleServicesBinding>()?.run {
                viewPager.apply {
                    orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }

                tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabReselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        tab?.icon = null
                        val text = tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        text?.setTypeface(null, Typeface.NORMAL)
                    }

                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.position?.let {
                            val tabLayout = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(it) as? LinearLayout
                            val tabTextView = tabLayout?.getChildAt(1) as TextView
                            tabTextView.setTypeface(tabTextView.typeface, Typeface.BOLD)
                        }
                    }
                })
                if (!bundles.isNullOrEmpty()) {
                    viewPager.adapter = object : FragmentStateAdapter(this@MyBundleServicesVPFragment) {

                        override fun createFragment(position: Int): Fragment {
                            return MyBundlesAndServicesFragment.newInstance(bundles.getOrNull(position)?.myBundlesInfos)
                        }

                        override fun getItemCount(): Int {
                            return bundles.size
                        }
                    }
                    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                        tab.text = bundles.getOrNull(position)?.titles
                    }.attach()
                } else {
                    showInlineMessage(getString(R.string.you_are_not_subscribed_to_any_bundles))
                }
            }
        }

    }

}

