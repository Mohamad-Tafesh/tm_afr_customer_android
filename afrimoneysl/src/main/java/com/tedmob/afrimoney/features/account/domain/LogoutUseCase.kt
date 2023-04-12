package com.tedmob.afrimoney.features.account.domain

import com.tedmob.afrimoney.app.usecase.SuspendableUseCase
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase
@Inject constructor(
    private val session: SessionRepository
) : SuspendableUseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit) {
        return withContext(Dispatchers.IO) {
            session.invalidateSession()
        }
    }
}