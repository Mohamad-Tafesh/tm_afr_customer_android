package com.tedmob.africell.features.aboutus

import android.os.Bundle
import android.view.*
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.ui.viewmodel.ViewModelFactory
import com.tedmob.africell.ui.viewmodel.observeResourceInline
import com.tedmob.africell.ui.viewmodel.provideViewModel
import com.tedmob.africell.util.html.html
import com.tedmob.africell.util.intents.email
import com.tedmob.africell.util.intents.openWebsite
import com.tedmob.africell.util.intents.share
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.toolbar_image.*
import javax.inject.Inject

class AboutUsFragment : BaseFragment() {

    private val viewModel by provideViewModel<AboutViewModel> { viewModelFactory }
    var message: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return wrap(inflater.context, R.layout.fragment_about_us, R.layout.toolbar_image, true)
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
            message = about.description
            description.text = about.description.orEmpty().html()
          //  imageView.setImageURI(about.image)

            sendFeedBackBtn.setOnClickListener {
                email(to = about.email)
            }
            insta.setOnClickListener {
            //   openWebsite(about.ins, "Requested URL is not valid", null)
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
