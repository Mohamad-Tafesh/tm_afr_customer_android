package com.tedmob.afrimoney.features.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import coil.load
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.Message
import com.tedmob.afrimoney.databinding.FragmentMessagesBinding
import com.tedmob.afrimoney.databinding.ItemNotifMessageBinding
import com.tedmob.afrimoney.ui.button.setDebouncedOnClickListener
import com.tedmob.afrimoney.ui.viewmodel.provideViewModel
import com.tedmob.afrimoney.util.adapter.adapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : BaseVBFragment<FragmentMessagesBinding>() {

    private val viewModel by provideViewModel<MessagesInboxViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentMessagesBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
    }

    private sealed class MessageData(
        @DrawableRes val image: Int,
        @StringRes val title: Int,
        @StringRes val desc: Int
    ) {
        object About : MessageData(
            R.drawable.ic_field_contacts,
            R.string.about_afrimoney,
            R.string.about_afrimoney
        )

        object Terms :
            MessageData(R.drawable.ic_field_contacts, R.string.terms_of_use, R.string.terms_of_use)

        object Contact : MessageData(
            R.drawable.ic_field_contacts,
            R.string.contact_support,
            R.string.contact_support
        )

        object Help : MessageData(R.drawable.ic_field_contacts, R.string.help, R.string.help)
        object Refer : MessageData(
            R.drawable.ic_field_contacts,
            R.string.refer_a_friend,
            R.string.refer_a_friend
        )

        object FAQs : MessageData(R.drawable.ic_field_contacts, R.string.faq, R.string.faq)
        object Settings : MessageData(0, R.string.app_settings, R.string.app_settings)

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withVBAvailable {
            viewModel.getData()
            rvMessages.adapter = adapter(MessageData.values()) {
                viewBinding(ItemNotifMessageBinding::inflate)
                onBindItemToViewBinding<ItemNotifMessageBinding> { item ->
                    if (item.image == 0)
                        ivMessagePhoto.visibility = View.GONE
                    else
                        ivMessagePhoto.load(item.image)

                    tvMessageTitle.setText(item.title)
                    tvMessageDesc.setText(item.desc)

                    root.setDebouncedOnClickListener {
                        findNavController().navigate(
                            MessagesFragmentDirections.actionMessagesFragmentToMessagesDetailsFragment(
                                Message(
                                    item.image,
                                    resources.getString(item.title),
                                    resources.getString(item.desc)
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}