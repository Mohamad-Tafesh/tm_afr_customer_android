package com.tedmob.africell.features.home.domain


import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.requests.PlayerPushRequest
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SetUserPushUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, Unit>(schedulers) {

    override fun buildUseCaseObservable(params: Unit): Observable<Unit> {
        return oneSignalIdsObservable.flatMap {
            restApi.setUserPush(PlayerPushRequest( it))
        }
    }
}


private inline val oneSignalIdsObservable
    get() = Observable.create<String> {
        val userId = OneSignal.getDeviceState()?.userId
        if (!userId.isNullOrEmpty()) {
            //shortcut
            it.onNext(userId)
            it.onComplete()
        } else {
            val observer = object : OSSubscriptionObserver {
                override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
                    it.onNext(stateChanges?.to?.userId.orEmpty())
                    OneSignal.removeSubscriptionObserver(this)
                    it.onComplete()
                }
            }
            OneSignal.addSubscriptionObserver(observer)
        }
    }
        .subscribeOn(AndroidSchedulers.mainThread())