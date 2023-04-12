package com.tedmob.afrimoney.features.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.app.withVBAvailable
import com.tedmob.afrimoney.data.entity.Message
import com.tedmob.afrimoney.databinding.FragmentMessagesDetailsBinding

class MessagesDetailsFragment : BaseVBFragment<FragmentMessagesDetailsBinding>() {

    private val args by navArgs<MessagesDetailsFragmentArgs>()
    private lateinit var message: Message

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentMessagesDetailsBinding::inflate, false)
    }

    override fun configureToolbar() {
        actionbar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.message?.let {
            message = it
        }
        withVBAvailable {
            if (message.image == 0)
                ivMessagePhoto.visibility = View.GONE
            else
                message.image?.let { ivMessagePhoto.load(it) }

            message.messageTitle?.let {
                tvMessagesTitle.text = it
            }
            message.messageDesc?.let {
                tvMessagesDesc.text = it
            }
        }
    }
}