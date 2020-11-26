package com.tedmob.africell.data.api

import com.tedmob.africell.data.api.dto.LoginDTO
import com.tedmob.africell.data.api.requests.SignInRequestDTO
import com.tedmob.africell.data.entity.User
import io.reactivex.Observable
import retrofit2.http.*

interface RestApi {


    @POST("SelfCare/SignIN")
    fun login(
        @Body request: SignInRequestDTO,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<LoginDTO>



}