package com.tedmob.afrimoney.features.home.domain

import com.tedmob.afrimoney.app.usecase.ObservableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.features.account.UserAccountInfo
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.rxObservable
import javax.inject.Inject

class GetBalanceUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository,
) : ObservableUseCase<UserAccountInfo, String>() {

    override fun buildUseCaseObservable(params: String): Observable<UserAccountInfo> {
        return rxObservable(Dispatchers.IO) {
            /* lastBalance?.let { send(it) }

             send(
                 api.getBalance().also {
                     lastBalance = it
                 }
             )*/
            lastBalance?.let {
                send(
                    UserAccountInfo(
                        null,
                        it,
                        "0",
                        "0",
                        session.user?.name.orEmpty()
                    )
                )
            }
            val response2 = api.getBalance()
            lastBalance = response2.BALANCE.orEmpty().toDouble().toString()
            send(
                UserAccountInfo(
                    null,
                    response2.BALANCE.orEmpty(),
                    response2.FICBALANCE.orEmpty(),
                    response2.FRBALANCE.orEmpty(),
                    session.user?.name.orEmpty()
                )
                //UserAccountInfo(null, response2.BALANCE.orEmpty(),"1","0.5", session.user?.name.orEmpty())
            )

        }
    }
}

private var lastBalance: String? = null