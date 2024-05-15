package com.tedmob.afrimoney.features.home.domain

import com.tedmob.afrimoney.app.usecase.ObservableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.features.account.UserAccountInfo
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.rx2.rxObservable
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class GetBalanceUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository,
) : ObservableUseCase<UserAccountInfo, String>() {

    override fun buildUseCaseObservable(params: String): Observable<UserAccountInfo> {
        return rxObservable(Dispatchers.IO) {

            lastBalance?.let {
                send(it)
            }

            coroutineScope {
                val normalWalletAsync = async { api.getBalance("12") }
                val bonusWalletAsync = async { api.getBalance("17") }
                //val remittanceWalletAsync = async { api.getBalance("73") }//TEST
                val remittanceWalletAsync = async { api.getBalance("75") } //LIVE


            lastBalance = UserAccountInfo(
                normalWalletAsync.await().BALANCE.orEmpty().toDouble().toString(),
                bonusWalletAsync.await().BALANCE.orEmpty().toDouble().toString(),
                remittanceWalletAsync.await().BALANCE.orEmpty().toDouble().toString(),
                session.user?.name.orEmpty()
            )

            send(
                UserAccountInfo(
                    normalWalletAsync.await().BALANCE.orEmpty().toDouble().toString(),
                    bonusWalletAsync.await().BALANCE.orEmpty().toDouble().toString(),
                    remittanceWalletAsync.await().BALANCE.orEmpty().toDouble().toString(),
                    session.user?.name.orEmpty()
                )
            )
        }
        }
    }
}

private var lastBalance: UserAccountInfo? = null