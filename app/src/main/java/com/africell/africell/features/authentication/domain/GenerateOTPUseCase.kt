package com.africell.africell.features.authentication.domain

import com.africell.africell.app.ExecutionSchedulers
import com.africell.africell.app.UseCase
import com.africell.africell.data.api.ApiContract.Params.FORGOT_PASSWORD_TYPE
import com.africell.africell.data.api.ApiContract.Params.NEW_USER_TYPE
import com.africell.africell.data.api.RestApi
import com.africell.africell.data.api.dto.GenerateOTPDTO
import com.africell.africell.data.api.requests.GenerateOTPRequest
import com.africell.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import javax.inject.Inject

class GenerateOTPUseCase
@Inject constructor(
    private val api: RestApi,
    private val session: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<GenerateOTPDTO, GenerateOTPUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<GenerateOTPDTO> {
        return api.generateOTP(GenerateOTPRequest(params.username, params.typeOfOTP)).map {
            if(params.typeOfOTP== NEW_USER_TYPE || params.typeOfOTP== FORGOT_PASSWORD_TYPE ){
                session.msisdn = params.username
            }
            it
        }

    }

    data class Params(val username: String,val typeOfOTP:Int)
}
