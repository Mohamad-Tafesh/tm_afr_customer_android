package com.tedmob.africell.app

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.tedmob.africell.data.repository.domain.SessionRepository
import com.tedmob.africell.exception.AppException
import com.tedmob.africell.features.launch.RootActivity
import dagger.Reusable
import javax.inject.Inject

@Reusable
class AppSessionNavigator
@Inject constructor(
    private val session: SessionRepository,
    private val application: Application
) {

    fun invalidateSessionAndRestart(exceptionToShow: AppException) {
        session.invalidateSession()
        Toast.makeText(application, exceptionToShow.userMessage, Toast.LENGTH_LONG).show()
        restart()
    }

    fun invalidateSessionAndRestart() {
        session.invalidateSession()
        restart()
    }

    fun restart() {
        application.startActivity(
            Intent(application, RootActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }
        )
    }
}