package com.tedmob.africelll.features.bundles

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
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.BundleCategoriesDTO
import com.tedmob.africell.data.api.dto.BundlesDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.features.bundles.BundlesFragment
import com.tedmob.africell.features.bundles.BundlesViewModel
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import kotlinx.android.synthetic.main.fragment_bundle_vp.*
import javax.inject.Inject


class BundleVPFragment : BaseFragment() {
    val bundleCategory by lazy {
        arguments?.getParcelable<BundleCategoriesDTO>(KEY_BUNDLE_CATEGORY)
            ?: throw IllegalArgumentException("required bundle arguments")
    }

    @Inject
    lateinit var sessionRepository: SessionRepository

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by provideViewModel<BundlesViewModel> { viewModelFactory }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_bundle_vp, R.layout.toolbar_default, true)
    }

    companion object {
        const val KEY_BUNDLE_CATEGORY = "bundle_category"
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.show()
        actionbar?.title = ""
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()

    }

    private fun bindData() {
        viewModel.getBundlesByCategory(bundleCategory.idBundleCategories)
        observeResourceInline(viewModel.bundlesData) { bundles ->
            setupViewPager(bundles)
        }
    }

    private fun setupViewPager(bundles: List<BundlesDTO>) {


        viewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return BundlesFragment.newInstance(bundles[position].bundleInfo)
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
            tab.text = bundles[position].bundleTypeName
        }.attach()
    }

}

