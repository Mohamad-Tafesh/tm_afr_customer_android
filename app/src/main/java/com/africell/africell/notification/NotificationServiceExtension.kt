package com.africell.africell.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.onesignal.OSNotificationReceivedEvent
import com.onesignal.OneSignal
import com.tedmob.afrimoney.R


@Suppress("unused") //this class is being used in Manifest
class NotificationServiceExtension : OneSignal.OSRemoteNotificationReceivedHandler {

    override fun remoteNotificationReceived(
        context: Context?,
        notificationReceivedEvent: OSNotificationReceivedEvent?
    ) {
        val notification = notificationReceivedEvent?.notification
        if (notification?.launchURL.isNullOrEmpty()) {
            //no url, just rely on the default behavior
            notificationReceivedEvent?.complete(notification)
        } else {
            //there is a url, add a button to open the link
            val newNotification = notification?.mutableCopy()
                ?.apply {
                    setExtender {
                        val urlIntent = Intent(Intent.ACTION_VIEW, launchURL.toUri())
                        it.addAction(
                            NotificationCompat.Action(
                                R.drawable.ic_action_open_link,
                                context?.getString(R.string.open_link),
                                PendingIntent.getActivity(
                                    context,
                                    1,
                                    urlIntent,
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                    else
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                )
                            )
                        )
                    }
                }
            notificationReceivedEvent?.complete(newNotification)
        }
    }
}