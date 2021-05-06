package com.tedmob.africell.notification

import android.content.Context
import androidx.navigation.NavDeepLinkBuilder
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import com.tedmob.africell.R
import com.tedmob.africell.features.launch.PushFragmentArgs
import timber.log.Timber

class NotificationOpenedHandler(private val context: Context) :
    OneSignal.OSNotificationOpenedHandler  {
    override fun notificationOpened(result: OSNotificationOpenedResult) {
        Timber.d("Notification opened")
        val payload = result.notification

        val additionalData = payload.additionalData
        val imageWidth = additionalData?.optDouble("image_width", -1.0)?.takeIf { it >= 0.0 }
        val imageHeight = additionalData?.optDouble("image_height", -1.0)?.takeIf { it >= 0.0 }

        val notificationData = NotificationDataImpl(
            title = payload.title,
            message = payload.body,
            image = payload.bigPicture,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )

        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_root)
            .setDestination(R.id.pushFragment)
            .setArguments(PushFragmentArgs(notificationData).toBundle())
            .createPendingIntent()

        pendingIntent.send()
    }

}