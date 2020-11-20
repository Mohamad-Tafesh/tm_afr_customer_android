package com.tedmob.africell.data.api

import com.tedmob.africell.data.entity.User
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Tag

interface RestApi {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<User>
}