package com.tedmob.afrimoney.app

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.navigation.NavDeepLinkBuilder
import com.tedmob.afrimoney.R
import com.tedmob.afrimoney.data.SingleLiveEvent
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.features.authentication.EnterPinActivity
import com.tedmob.afrimoney.features.authentication.SetPinFragmentArgs
import com.tedmob.afrimoney.features.launch.RootActivity
import com.tedmob.afrimoney.features.newhome.AfrimoneyActivity
import dagger.Reusable
import javax.inject.Inject

@Reusable
class AppSessionNavigator
@Inject constructor(
    private val session: SessionRepository,
    private val application: Application,

    ) {

    var onAppRestarted: SingleLiveEvent<Unit> = SingleLiveEvent()
    fun invalidateSessionAndRestart(exceptionToShow: AppException) {
        session.accessToken = ""
        session.refreshToken = ""
        session.user = null
        Toast.makeText(application, exceptionToShow.userMessage, Toast.LENGTH_LONG).show()
        restart()
    }

    fun invalidateSessionAndRestart() {
        session.invalidateSession()
        restart()
    }


    fun restart() {

        application.startActivity(
            Intent(application, AfrimoneyActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                )
                putExtra("restart", 401)
            }
        )
    }

    fun resetPin() {

        NavDeepLinkBuilder(application)
            .setComponentName(RootActivity::class.java)
            .setGraph(R.navigation.nav_root)
            .setDestination(R.id.setPinFragment)
            .setArguments(SetPinFragmentArgs(session.msisdn).toBundle())
            .createPendingIntent()
            .send()
    }

    fun openPinScreen(exceptionToShow: AppException? = null) {
        exceptionToShow?.let {
            Toast.makeText(application, it.userMessage, Toast.LENGTH_LONG).show()
        }
        application.startActivity(
            Intent(application, EnterPinActivity::class.java)
        )
    }
}


inline fun AppException.handleInvalidSession(
    appSessionNavigator: AppSessionNavigator,
    onOtherError: (exception: AppException) -> Unit = {}
): Boolean {
    return if (status == 401) {
        appSessionNavigator.invalidateSessionAndRestart(this)
        true
    } else when (code) {
        AppException.Code.INVALID_PIN -> {
            appSessionNavigator.openPinScreen(this)
            true
        }

        else -> {
            onOtherError(this)
            false
        }
    }
}