package com.tedmob.africell.features.launch

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tedmob.africell.R
import com.tedmob.africell.app.BaseFragment
import com.tedmob.africell.notification.NotificationData
import com.tedmob.africell.ui.image.setImageUriWithDimens
import com.tedmob.africell.util.html.html
import kotlinx.android.synthetic.main.fragment_push.*

class PushFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return wrap(inflater.context, R.layout.fragment_push, 0, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val notificationData = PushFragmentArgs.fromBundle(arguments!!).notificationData
        showNotification(notificationData)
        message.movementMethod = LinkMovementMethod.getInstance()

        closeButton.setOnClickListener { activity?.finish() }
        goToAppButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun showNotification(notificationData: NotificationData) {
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