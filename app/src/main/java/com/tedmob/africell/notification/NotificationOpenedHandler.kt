package com.tedmob.africell.notification

import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import com.tedmob.africell.R
import com.tedmob.africell.features.accountInfo.AccountInfoActivity
import com.tedmob.africell.features.bundles.BundleActivity
import com.tedmob.africell.features.bundles.BundleDetailsFragment
import com.tedmob.africell.features.bundles.BundleVPFragment
import com.tedmob.africell.features.home.MainActivity
import com.tedmob.africell.features.launch.PushFragmentArgs
import com.tedmob.africell.features.location.LocationDetailsFragment
import com.tedmob.africell.features.services.ServiceDetailsFragment
import timber.log.Timber

class NotificationOpenedHandler(private val context: Context) :
    OneSignal.OSNotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenedResult) {
        Timber.d("Notification opened")
        val payload = result.notification
        val additionalData = payload.additionalData
        val imageWidth = additionalData?.optDouble("image_width", -1.0)?.takeIf { it >= 0.0 }
        val imageHeight = additionalData?.optDouble("image_height", -1.0)?.takeIf { it >= 0.0 }
        val itemId = additionalData?.optString("item_id")
        val type = additionalData?.optString("type")
        when (type) {
            "new_bundle" -> {
                val bundle = bundleOf(BundleDetailsFragment.BUNDLE_ID to itemId)
                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_bundle)
                    .setComponentName(BundleActivity::class.java)
                    .setDestination(R.id.bundleDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

                pendingIntent.send()
            }
            "new_category" -> {
                val bundle = bundleOf(BundleVPFragment.KEY_BUNDLE_CATEGORY_ID to itemId)
                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_bundle)
                    .setComponentName(BundleActivity::class.java)
                    .setDestination(R.id.bundleVPFragment)
                    .setArguments(bundle)
                    .createPendingIntent()
                pendingIntent.send()
            }
            "new_service" -> {
                val bundle = bundleOf(ServiceDetailsFragment.KEY_SNAME to itemId)
                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_main)
                    .setComponentName(MainActivity::class.java)
                    .setDestination(R.id.serviceDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()
                pendingIntent.send()
            }
            "new_location" -> {
                val bundle = bundleOf(LocationDetailsFragment.LOCATION_ID to itemId)
                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_main)
                    .setComponentName(MainActivity::class.java)
                    .setDestination(R.id.locationDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()
                pendingIntent.send()
            }
            "account" -> {
                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_account)
                    .setComponentName(AccountInfoActivity::class.java)
                    .setDestination(R.id.accountFragment)
                    .createPendingIntent()
                pendingIntent.send()
            }
            "profile" -> {
                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_account)
                    .setComponentName(AccountInfoActivity::class.java)
                    .setDestination(R.id.editProfileFragment)
                    .createPendingIntent()
                pendingIntent.send()
            }

            "book" -> {
                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_main)
                    .setComponentName(MainActivity::class.java)
                    .setDestination(R.id.bookNumberFragment)
                    .createPendingIntent()
                pendingIntent.send()
            }

            "update" -> {
                openAppInGooglePlay(context)
            }
            "bulk" -> {
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
            else -> {
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


    }

    private fun openAppInGooglePlay(context: Context) {
        val packageName = /*context.packageName*/ "com.africell.africell.africellapp"
        try {
            PendingIntent.getActivity(
                context,
                1,
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")),
                PendingIntent.FLAG_UPDATE_CURRENT
            ).send()
        //    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (ex: ActivityNotFoundException) {

            PendingIntent.getActivity(
                context,
                1,
                Intent(Intent.ACTION_VIEW,   Uri.parse("http://play.google.com/store/apps/details?id=$packageName")),
                PendingIntent.FLAG_UPDATE_CURRENT
            ).send()
        }

    }

}