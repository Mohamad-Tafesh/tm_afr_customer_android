package com.africell.africell.features.addNewAccount.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.SubAccountDTO
import com.africell.africell.data.api.requests.VerifyOTPRequest
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class AddSubAccountUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val session: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<SubAccountDTO, AddSubAccountUseCase.Params>(schedulers) {


    override fun buildUseCaseObservable(params: Params): Observable<SubAccountDTO> {
        return restApi.verifyOTP(VerifyOTPRequest( params.msisdn, params.pin)).flatMap {
         verify-> restApi.addSubAccount(verify?.verificationToken)
        }
    }

    data class Params(
        val msisdn:String,
        val pin: String
    )
}
