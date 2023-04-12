package com.tedmob.afrimoney.data.api

import com.tedmob.afrimoney.data.api.dto.LocationDTO
import okhttp3.Headers
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface AfrimoneyRestApi {

    @GET
    suspend fun get(
        @Url path: String,
        @HeaderMap headers: Headers,
        @QueryMap queries: Map<String, String>,
        @QueryName valueQueries: List<String>?,
    ): ResponseBody

    @POST
    suspend fun post(
        @Url path: String,
        @HeaderMap headers: Headers,
        @QueryMap queries: Map<String, String>,
        @QueryName valueQueries: List<String>?,
        @Body body: RequestBody,
    ): ResponseBody

    @GET
    suspend fun getShopLocation(
        //@Header("X-Authorization") authorization: String,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contenttype: String = "application/json",
        @Header("accept") accept: String = "text/plain",
        @Header("Accept-Language") lang: String = "en",
        @Url url: String = "https://selfcareapp.africell.sl/SelfCare/GetShopLocation?SearchShop=",
    ): List<LocationDTO>
}