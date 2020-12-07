package com.tedmob.africell.features.authentication.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.GenerateOTPDTO
import com.tedmob.africell.data.api.requests.GenerateOTPRequest
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Login user using name, email and phone number
 *
 * Save session when successful
 */
class GenerateOTPUseCase
@Inject constructor(
    private val api: RestApi,
    private val session: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<GenerateOTPDTO, GenerateOTPUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<GenerateOTPDTO> {
        return api.generateOTP(GenerateOTPRequest(params.username, 0)).map {
            session.msisdn = params.username
            it
        }

    }

    data class Params(val username: String)
}
