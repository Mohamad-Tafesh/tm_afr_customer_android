package com.tedmob.afrimoney.features.account.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.api.TedmobApis
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.features.account.UserAccountInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserAccountInfoUseCase
@Inject constructor(
    private val api: TedmobApis,
    private val session: SessionRepository
) : SuspendableUseCase<UserAccountInfo, Unit>() {

    override suspend fun execute(params: Unit): UserAccountInfo {
        return withContext(Dispatchers.IO) {
            val userAsync = async { api.userInfo(session.msisdn) }
            val balanceAsync = async { api.getBalance("12") }

            val userResponse = userAsync.await()
            val balanceResponse = balanceAsync.await()
            UserAccountInfo(
                balanceResponse.BALANCE.orEmpty(),
                balanceResponse.FICBALANCE.orEmpty(),
                balanceResponse.FRBALANCE.orEmpty(),
                userResponse.name.orEmpty(),
            )
        }
    }
}