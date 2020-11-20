package com.tedmob.africell.notification

import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult

class NotificationService : NotificationExtenderService() {
    override fun onNotificationProcessing(notification: OSNotificationReceivedResult): Boolean {
        // this demonstrates how to intercept a notification
        return if (notification.payload.title == "Test") {
            val overrideSettings = OverrideSettings()
            overrideSettings.extender = NotificationCompat.Extender { builder -> builder.setColor(Color.GREEN) }
            displayNotification(overrideSettings)
            // don't show notification we've already handled it
            true
        } else
            false // show default notification
    }
}