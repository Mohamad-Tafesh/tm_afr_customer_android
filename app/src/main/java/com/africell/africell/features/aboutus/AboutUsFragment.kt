package com.africell.africell.features.aboutus

import android.os.Bundle
import android.view.*
import com.africell.africell.R
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentAboutUsBinding
import com.africell.africell.databinding.ToolbarImageBinding
import com.africell.africell.ui.viewmodel.observeResourceInline
import com.africell.africell.ui.viewmodel.provideViewModel
import com.africell.africell.util.html.html
import com.africell.africell.util.intents.email
import com.africell.africell.util.intents.openWebsite
import com.africell.africell.util.intents.share
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment : BaseVBFragment<FragmentAboutUsBinding>() {

    private val viewModel by provideViewModel<AboutViewModel>()
    var message: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentAboutUsBinding::inflate, true, ToolbarImageBinding::inflate)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.aboutUs()
        bindAboutData()
    }

    override fun configureToolbar() {
        super.configureToolbar()
        actionbar?.title = ""
        actionbar?.setHomeAsUpIndicator(R.mipmap.nav_side_menu)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_about, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
                share("", "https://www.africell.com/")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun bindAboutData() {
        observeResourceInline(viewModel.aboutData) { about ->
            withVBAvailable {
                message = about.description
                description.text = about.description.orEmpty().html()
                //  imageView.setImageURI(about.image)

                sendFeedBackBtn.setOnClickListener {
                    email(to = about.email)
                }
                insta.setOnClickListener {
                    openWebsite(about.instagram, "Requested URL is not valid", null)
                }
                twitter.setOnClickListener {
                    openWebsite(about.twitter, "Requested URL is not valid", null)
                }
                facebook.setOnClickListener {
                    openWebsite(about.facebook, "Requested URL is not valid", null)
                }
                linkedIn.setOnClickListener {
                    openWebsite(about.linkedin, "Requested URL is not valid", null)
                }
                youtube.setOnClickListener {
                    openWebsite(about.youtube, "Requested URL is not valid", null)
                }
            }
        }
    }
}
