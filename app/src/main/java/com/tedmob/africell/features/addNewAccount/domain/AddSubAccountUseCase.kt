package com.tedmob.africell.features.addNewAccount.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.api.dto.SubAccountDTO
import com.tedmob.africell.data.api.requests.VerifyOTPRequest
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class AddSubAccountUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<SubAccountDTO, AddSubAccountUseCase.Params>(schedulers) {


    override fun buildUseCaseObservable(params: Params): Observable<SubAccountDTO> {
        return restApi.verifyOTP(VerifyOTPRequest( session.msisdn, params.pin)).flatMap {
         verify-> restApi.addSubAccount(verify?.verificationToken)
        }
    }

    data class Params(
        val pin: String
    )
}
