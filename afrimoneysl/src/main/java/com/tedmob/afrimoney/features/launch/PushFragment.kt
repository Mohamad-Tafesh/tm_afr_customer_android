package com.tedmob.afrimoney.features.launch

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.app.BaseVBFragment
import com.tedmob.afrimoney.databinding.FragmentPushBinding
import com.tedmob.afrimoney.notification.NotificationData
import com.tedmob.afrimoney.ui.image.optionalSize
import com.tedmob.afrimoney.ui.image.setAspectRatio
import com.tedmob.afrimoney.util.html.html
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PushFragment : BaseVBFragment<FragmentPushBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentPushBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationData = PushFragmentArgs.fromBundle(requireArguments()).notificationData
        showNotification(notificationData)
        requireBinding().message.movementMethod = LinkMovementMethod.getInstance()
        requireBinding().closeButton.setOnClickListener { activity?.finish() }
        requireBinding().goToAppButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun showNotification(notificationData: NotificationData) {
        actionbar?.title = notificationData.title

        viewLifecycleOwner.lifecycleScope.launch {
            if (notificationData.image == null) {
                requireBinding().image.visibility = View.GONE
            } else {
                requireBinding().image.run {
                    if (notificationData.imageWidth != null && notificationData.imageHeight != null) {
                        setAspectRatio(notificationData.imageWidth / notificationData.imageHeight)
                    } else {
                        setAspectRatio(1.67)
                    }
                    load(notificationData.image) {
                        placeholder(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.placeholder)))
                        optionalSize(notificationData.imageWidth?.toInt(), notificationData.imageHeight?.toInt())
                        scale(Scale.FIT)
                    }
                }
            }
        }

        val combinedMessage = notificationData.url
            ?.takeIf { it.isNotEmpty() }
            ?.sanitizeUrl()
            ?.let { "$it\n\n${notificationData.message?.html()}" }
            ?: notificationData.message?.html()
        requireBinding().message.text = combinedMessage
    }

    private inline fun String.sanitizeUrl(): String =
        if (!startsWith("https://")) "https://$this" else this
}