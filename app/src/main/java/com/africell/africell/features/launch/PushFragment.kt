package com.africell.africell.features.launch

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.africell.africell.app.viewbinding.BaseVBFragment
import com.africell.africell.app.viewbinding.withVBAvailable
import com.africell.africell.databinding.FragmentPushBinding
import com.africell.africell.notification.NotificationData
import com.africell.africell.ui.image.setImageUriWithDimens
import com.africell.africell.util.html.html
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PushFragment : BaseVBFragment<FragmentPushBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewBinding(container, FragmentPushBinding::inflate, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val notificationData = PushFragmentArgs.fromBundle(requireArguments()).notificationData
        withVBAvailable {
            showNotification(notificationData)
            message.movementMethod = LinkMovementMethod.getInstance()

            closeButton.setOnClickListener { activity?.finish() }
            goToAppButton.setOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun FragmentPushBinding.showNotification(notificationData: NotificationData) {
        actionbar?.title = notificationData.title()
        if (notificationData.image() == null) {
            image.visibility = View.GONE
        } else {
            image.setImageUriWithDimens(
                notificationData.image(),
                notificationData.imageWidth(),
                notificationData.imageHeight()
            )
        }
        message.text = notificationData.message()?.html()
    }

}