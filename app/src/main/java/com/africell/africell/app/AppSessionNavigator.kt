package com.africell.africell.app

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.africell.africell.data.repository.domain.SessionRepository
import com.africell.africell.exception.AppException
import com.africell.africell.exception.AppExceptionFactory
import com.africell.africell.features.authentication.domain.RefreshTokenUseCase
import com.africell.africell.features.launch.RootActivity
import dagger.Reusable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

@Reusable
class AppSessionNavigator
@Inject constructor(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val session: SessionRepository,
    private val appExceptionFactory: AppExceptionFactory,
    private val application: Application
) {

    fun invalidateSessionAndRestart(exceptionToShow: AppException) {
        session.invalidateSession()
        debugOnly {
            Toast.makeText(application, exceptionToShow.userMessage, Toast.LENGTH_LONG).show()
        }
        restart()
    }

    fun refreshToken() {
        refreshTokenUseCase.execute(Unit, object : DisposableObserver<Unit>() {
            override fun onNext(t: Unit) {
                debugOnly {
                    Toast.makeText(application, "refreshed Token", Toast.LENGTH_LONG).show()
                }
            }

            override fun onError(e: Throwable) {
                val appException = appExceptionFactory.make(e)
                when (appException.message) {
                    "Invalid_token" -> invalidateSessionAndRestart()
                    else -> onError(appException)
                }
            }

            override fun onComplete() {

            }

        })
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