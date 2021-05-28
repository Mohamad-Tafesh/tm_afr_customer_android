package com.tedmob.africell.features.bundles

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
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.dto.BundlesDTO
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_bundle_vp.*
import kotlinx.android.synthetic.main.fragment_location.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class BundleVPFragment : BaseFragment() {


    val bundleId by lazy {
        arguments?.getString(KEY_BUNDLE_CATEGORY_ID)
            ?: throw IllegalArgumentException("required bundle arguments")
    }
    val bundleName by lazy {
        arguments?.getString(KEY_CATEGORY_NAME)
    }
    @Inject
    lateinit var sessionRepository: SessionRepository


    private val viewModel by provideViewModel<BundlesViewModel> { viewModelFactory }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_bundle_vp, R.layout.toolbar_bundle_vp, true)
    }

    companion object {
        const val KEY_CATEGORY_NAME = "key_category_name"
        const val KEY_BUNDLE_CATEGORY_ID= "bundle_category_id"
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.show()
        actionbar?.title = bundleName.orEmpty()+ " Bundles"
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_back)
        setHasOptionsMenu(true)
    }
    private fun searchRxTextView() {
        searchTxt?.let {
            RxTextView.textChanges(it)
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ charSequence ->
                    viewModel.getBundlesByCategory(bundleId,charSequence.toString())
                }) { t -> }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchRxTextView()
        bindData()


    }

    private fun bindData() {
        viewModel.getBundlesByCategory(bundleId,null)
        observeResourceInline(viewModel.bundlesData) { bundles ->

            if(bundleName.isNullOrEmpty()) {
                actionbar?.title = bundles.getOrNull(0)?.bundleInfo?.getOrNull(0)?.category.orEmpty() + " Bundles"
            }
            setupViewPager(bundles)
        }
    }

    private fun setupViewPager(bundles: List<BundlesDTO>) {

        viewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun createFragment(position: Int): Fragment {
                return BundlesFragment.newInstance(bundles[position])
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

