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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.ApiContract
import com.africell.africell.data.api.dto.MyBundlesAndServices
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_my_bundles_services_vp.*
import kotlinx.android.synthetic.main.toolbar_bundle_services.*
import kotlinx.android.synthetic.main.toolbar_bundle_services.toolbarImage
import javax.inject.Inject


class MyBundleServicesVPFragment : BaseFragment() {

    @Inject
    lateinit var sessionRepository: SessionRepository


    private val viewModel by provideViewModel<MyBundlesAndServicesViewModel> { viewModelFactory }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_my_bundles_services_vp, R.layout.toolbar_bundle_services, true)
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
        setupImageBanner(toolbarImage, ApiContract.Params.BANNERS, ApiContract.ImagePageName.BUNDLES_SERVICES)
        bindData()

    }

    private fun bindData() {
        viewModel.getMyBundlesService()
        observeResourceInline(viewModel.myBundlesAndServicesData) { bundles ->
            setupViewPager(bundles)
        }
    }

    private fun setupViewPager(bundles: List<MyBundlesAndServices>) {


        viewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return MyBundlesAndServicesFragment.newInstance(bundles[position].myBundlesInfos)
            }

            override fun getItemCount(): Int {
                return bundles.size
            }
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
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = bundles[position].titles
        }.attach()
    }

}

