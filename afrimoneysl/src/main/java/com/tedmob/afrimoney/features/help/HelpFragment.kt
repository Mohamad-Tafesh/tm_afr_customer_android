package com.tedmob.afrimoney.features.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.databinding.FragmentHelpBinding
import com.tedmob.afrimoney.databinding.ItemViewPagerBinding
import com.tedmob.afrimoney.util.adapter.adapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : BaseVBFragment<FragmentHelpBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun configureToolbar() {
        actionbar?.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentHelpBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withVBAvailable {
            viewPager.adapter = adapter(ViewPagerScreens.values()) {
                viewBinding(ItemViewPagerBinding::inflate)
                onBindItemToViewBinding<ItemViewPagerBinding> {
                    tvTitle.setText(it.title)
                    tvDesc.setText(it.desc)
                }
            }
            indicator.setViewPager(viewPager)
        }
    }

    private sealed class ViewPagerScreens(
        @StringRes val title: Int,
        @StringRes val desc: Int
    ) {
        object About : ViewPagerScreens(R.string.about_afrimoney, R.string.about_afrimoney)
        object Terms : ViewPagerScreens(R.string.terms_of_use, R.string.terms_of_use)
        object Contact : ViewPagerScreens(R.string.contact_support, R.string.contact_support)
        object Help : ViewPagerScreens(R.string.help, R.string.help)
        object Refer : ViewPagerScreens(R.string.refer_a_friend, R.string.refer_a_friend)
        object FAQs : ViewPagerScreens(R.string.faq, R.string.faq)
        object Settings : ViewPagerScreens(R.string.app_settings, R.string.app_settings)

        companion object {
            fun values() = listOf(
                About,
                Terms,
                Contact,
                Help,
                Refer,
                FAQs,
                Settings,
            )
        }
    }
}