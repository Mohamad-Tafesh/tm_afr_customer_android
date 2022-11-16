package com.africell.africell.features.afrimoneyBundles

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding2.widget.RxTextView
import com.africell.africell.R
import com.africell.africell.app.BaseFragment
import com.africell.africell.data.api.dto.BundlesDTO
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_bundle_vp.*
import kotlinx.android.synthetic.main.toolbar_bundle_vp.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AfrimoneyBundleVPFragment : BaseFragment() {


    val bundleId by lazy {
        arguments?.getString(KEY_BUNDLE_CATEGORY_ID)
            ?: throw IllegalArgumentException("required bundle arguments")
    }
    val bundleName by lazy {
        arguments?.getString(KEY_CATEGORY_NAME)
    }

    val primaryColor by lazy {
        arguments?.getString(KEY_PRIMARY_COLOR_HEX)
    }

    val secondaryColor by lazy {
        arguments?.getString(KEY_SECONDARY_COLOR_HEX)
    }

    @Inject
    lateinit var sessionRepository: SessionRepository


    private val viewModel by provideViewModel<AfrimoneyBundlesViewModel> { viewModelFactory }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_bundle_vp, R.layout.toolbar_bundle_vp, true)
    }

    companion object {
        const val KEY_CATEGORY_NAME = "key_category_name"
        const val KEY_BUNDLE_CATEGORY_ID = "bundle_category_id"
        const val KEY_PRIMARY_COLOR_HEX = "primary_color"
        const val KEY_SECONDARY_COLOR_HEX = "secondary_color"
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.show()
        actionbar?.title = bundleName.orEmpty() + getString(R.string._bundles)
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
                    viewModel.getBundlesByCategory(bundleId, charSequence.toString())
                }) { t -> }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchRxTextView()
        bindData()


        changeBgdColor(primaryColor, secondaryColor)
    }

    private fun changeBgdColor(primaryColor:String?,secondaryColor:String?) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                activity?.window?.statusBarColor = Color.parseColor(primaryColor)
            }
            tabLayout.setBackgroundColor(Color.parseColor(primaryColor))
            toolbarLayout.setBackgroundColor(Color.parseColor(primaryColor))
            searchTextLayout.boxBackgroundColor = Color.parseColor(secondaryColor)
            titleTxt.setTextColor(Color.parseColor(secondaryColor))
        } catch (e: Exception) {

        }
    }

    private fun bindData() {
        viewModel.getBundlesByCategory(bundleId, null)
        observeResourceInline(viewModel.bundlesData) { bundles ->
            val firstPrimaryKey = bundles.getOrNull(0)?.bundleInfo?.getOrNull(0)?.primaryColor ?: primaryColor
            val firstSecondaryPrimaryKey = bundles.getOrNull(0)?.bundleInfo?.getOrNull(0)?.secondaryColor ?: secondaryColor
          changeBgdColor(firstPrimaryKey,firstSecondaryPrimaryKey)
            if (bundleName.isNullOrEmpty()) {
                actionbar?.title = bundles.getOrNull(0)?.bundleInfo?.getOrNull(0)?.category.orEmpty() + getString(R.string._bundles)
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
                return AfrimoneyBundlesFragment.newInstance(bundles[position])
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

