package com.tedmob.africell.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal


@Suppress("unused") //this class is being used in Manifest
class NotificationServiceExtension : OneSignal.OSRemoteNotificationReceivedHandler {

    override fun remoteNotificationReceived(
        context: Context?,
        notificationReceivedEvent: OSNotificationReceivedEvent?
    ) {
        val notification = notificationReceivedEvent?.notification

        // Example of modifying the notification
        val mutableNotification = notification?.mutableCopy()
        mutableNotification?.setExtender { builder: NotificationCompat.Builder ->
            builder
        }

        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notifiation, pass `null` to complete()
        notificationReceivedEvent?.complete(mutableNotification)
    }
}