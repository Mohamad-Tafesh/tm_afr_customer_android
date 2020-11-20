package com.tedmob.africell.features.authentication.domain

import com.tedmob.africell.app.ExecutionSchedulers
import com.tedmob.africell.app.UseCase
import com.tedmob.africell.data.api.ApiContract
import com.tedmob.africell.data.api.RestApi
import com.tedmob.africell.data.repository.domain.SessionRepository
import io.reactivex.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class RegisterUseCase
@Inject constructor(
    private val restApi: RestApi,
    private val sessionRepository: SessionRepository,
    schedulers: ExecutionSchedulers
) : UseCase<Unit, RegisterUseCase.Params>(schedulers) {

    override fun buildUseCaseObservable(params: Params): Observable<Unit> {

/*
        val firstName = MultipartBody.Part.createFormData(ApiContract.Param.FIRST_NAME, params.firstName.orEmpty())
        val lastName = MultipartBody.Part.createFormData(ApiContract.Param.LAST_NAME, params.lastName.orEmpty())
        val mobileNumber=MultipartBody.Part.createFormData(ApiContract.Param.MOBILE, params.mobileNumber.orEmpty())
        val companyName=MultipartBody.Part.createFormData(ApiContract.Param.COMPANY, params.companyName.orEmpty())
        val industry=MultipartBody.Part.createFormData(ApiContract.Param.MOBILE, params.industry.orEmpty())
        val jobRole=MultipartBody.Part.createFormData(ApiContract.Param.MOBILE, params.jobRole.orEmpty())
        val careerLevel=MultipartBody.Part.createFormData(ApiContract.Param.MOBILE, params.careerLevel.orEmpty())
        val email=MultipartBody.Part.createFormData(ApiContract.Param.MOBILE, params.email.orEmpty())
        val password=MultipartBody.Part.createFormData(ApiContract.Param.MOBILE, params.password.orEmpty())
        val confirmPassword=MultipartBody.Part.createFormData(ApiContract.Param.C_PASSWORD, params.confirmPassword.orEmpty())

*/
         /*restApi.register(firstName, lastName, mobileNumber,companyName,industry ,jobRole, careerLevel, email, password, confirmPassword)*/
        return Observable.just(Unit)
          /*  .map {
                //sessionRepository.isCompletedProfile = true
                sessionRepository.user = it
                it
            }*/
    }

    data class Params(
        val firstName: String?,
        val lastName: String?,
        val mobileNumber:String?,
        val companyName: String?,
        val email: String?,
        val password: String?,
        val confirmPassword:String?)
}