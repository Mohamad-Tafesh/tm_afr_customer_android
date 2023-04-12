package com.tedmob.afrimoney.features.aboutus

import android.os.Bundle
import android.view.*
import androidx.core.text.parseAsHtml
import coil.loadAny
import coil.size.Scale
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.AboutData
import com.tedmob.afrimoney.databinding.FragmentAboutUsBinding
import com.tedmob.afrimoney.databinding.ItemSocialLinkBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.observeResourceInline
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter
import com.tedmob.afrimoney.util.getText
import com.tedmob.afrimoney.util.intents.email
import com.tedmob.afrimoney.util.intents.openWebsite
import com.tedmob.afrimoney.util.intents.share
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment : BaseVBFragment<FragmentAboutUsBinding>() {

    private val viewModel by provideViewModel<AboutUsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun configureToolbar() {
        actionbar?.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createViewBinding(container, FragmentAboutUsBinding::inflate, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()

        viewModel.getData()
    }

    private fun bindData() {
        observeResourceInline(viewModel.content) {
            withVBAvailable {
                setupSocialLinks(it.socialLinks)
                setupText(it.text)
                sendFeedbackButton.setDebouncedOnClickListener { _ ->
                    sendFeedback(it.email) }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_about, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.shareAction -> {
            share(
                "Afrimoney",
                "Download the app here: ...",
                forceChooser = true,
                chooserTitle = "Please choose..."
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun FragmentAboutUsBinding.setupSocialLinks(links: List<AboutData.SocialLink>) {
        socialRV.adapter = adapter(links) {
            viewBinding(ItemSocialLinkBinding::inflate)
            onBindItemToViewBinding<ItemSocialLinkBinding> {
                socialButton.loadAny(it.image) {
                    scale(Scale.FIT)
                    lifecycle(viewLifecycleOwner)
                }
                socialButton.setOnClickListener { _ -> openWebsite(it.link) }
            }
        }
    }

    private fun FragmentAboutUsBinding.setupText(text: String) {
        aboutText.text = text.parseAsHtml()
    }


    private fun sendFeedback(email: String) {
        email(
            email,
            subject = "Afrimoney",
            message = "",
        )
    }
}
