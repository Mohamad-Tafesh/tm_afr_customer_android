package com.tedmob.africell.data.api

import com.tedmob.africell.data.api.dto.*
import com.tedmob.africell.data.api.requests.*
import com.tedmob.africell.features.faq.FaqItem
import io.reactivex.Observable
import retrofit2.http.*

interface RestApi {


    @POST("SelfCare/SignIN")
    fun login(
        @Body request: LoginRequest,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<LoginDTO>

    @POST("SelfCare/GenerateOTP")
    fun generateOTP(
        @Body request: GenerateOTPRequest,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<GenerateOTPDTO>

    @POST("SelfCare/VerifyOTP")
    fun verifyOTP(
        @Body request: VerifyOTPRequest,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<VerifyOTPDTO>

    @POST("SelfCare/ForgotPassword")
    fun resetPassword(
        @Body request: ForgotPasswordRequest,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<ResetPasswordDTO>


    @POST("SelfCare/SignUP")
    fun register(
        @Body request: RegisterRequest,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<RegisterDTO>

    @POST("SelfCare/UpdateProfileInfo")
    fun editProfile(
        @Body request: EditProfileRequest
    ): Observable<StatusDTO>

    @GET("SelfCare/GetSMSCount")
    fun getSMSCount(
        @Query("msisdn" ) msisdn:String
    ): Observable<SMSCountDTO>

    @POST("SelfCare/SendSMS")
    fun sendSMSCount(
        @Query("msisdn") msisdn:String?,
        @Query("receiverMsisdn" ) receiverMsisdn:String?,
        @Query("message" ) message :String?,
    ): Observable<SMSCountDTO>

    @GET("SelfCare/GetSupportCategory")
    fun getSupportCategory(): Observable<List<SupportCategoryDTO>>

    @POST("SelfCare/SendHelpMessage")
    fun sendHelpMessage(
        @Body request: HelpRequest,
    ): Observable<StatusDTO>


    @GET("SelfCare/GetIncidentType")
    fun getIncidentType(): Observable<List<SupportCategoryDTO>>

    @POST("SelfCare/SendIncident")
    fun sendIncident(
        @Body request: IncidentRequest,
    ): Observable<StatusDTO>

    @GET("SelfCare/GetQuestionAndResponse")
    fun getFaqs(): Observable<List<FaqItem>>

    @GET("SelfCare/GetUsefulNumbers")
    fun getUsefullNumber(): Observable<List<UsefulNumberDTO>>


    @GET("SelfCare/GetAllRechargedCard")
    fun getAllRechargedCard(): Observable<List<RechargeCardDTO>>

    @POST("SelfCare/VoucherRecharge")
    fun voucherRecharge(
        @Query("Sendermsisdn") msisdn:String?,
        @Query("receivermsisdn" ) receiverMsisdn:String?,
        @Query("voucherNumber" ) voucherNumber :String?,
    ): Observable<SMSCountDTO>

    @GET("SelfCare/GetProfileInfo")
    fun getProfile(): Observable<UserDTO>


    @GET("SelfCare/GetSubAccount")
    fun getSubAccount(): Observable<SubAccountDTO>

    @GET("SelfCare/GetSubAccount")
    fun getAccountBalance(
        @Query("SubMsisdn") msisdn:String?,
    ): Observable<AccountBalanceDTO>
}