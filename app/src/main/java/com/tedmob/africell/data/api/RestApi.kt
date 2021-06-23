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
        @Body request: GenerateOTPRequest
    ): Observable<GenerateOTPDTO>

    @POST("SelfCare/SignOUT")
    fun logout(
    ): Observable<Unit>

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


    @POST("SelfCare/ChangePassword")
    fun changePassword(
        @Body request: ChangePasswordRequest,
    ): Observable<ChangePasswordDTO>

    @POST("SelfCare/SignUP")
    fun register(
        @Body request: RegisterRequest,
        @Tag tag: String = ApiContract.Params.NO_TOKEN_TAG
    ): Observable<RegisterDTO>

    @POST("SelfCare/UpdateProfileInfo")
    fun editProfile(
        @Body request: EditProfileRequest
    ): Observable<StatusDTO>

    @GET("SelfCare/GetShopLocation")
    fun getShopLocation(
        @Query("SearchShop") searchShop: String?
    ): Observable<List<LocationDTO>>

    @GET("SelfCare/GetShopLocation/{id}")
    fun getShopLocationDetails(
        @Path("id") shopId: String?
    ): Observable<LocationDTO>

    @GET("SelfCare/GetAboutUs")
    fun aboutUs(
    ): Observable<AboutDTO>

    @GET("SelfCare/GetSMSCount")
    fun getSMSCount(
        @Query("msisdn") msisdn: String?
    ): Observable<SMSCountDTO>

    @POST("SelfCare/SendSMS")
    fun sendSMSCount(
        @Query("msisdn") msisdn: String?,
        @Query("receiverMsisdn") receiverMsisdn: String?,
        @Query("message") message: String?,
    ): Observable<SMSCountDTO>

    @GET("SelfCare/GetSupportCategory")
    fun getSupportCategory(): Observable<List<SupportCategoryDTO>>

    @POST("SelfCare/SendHelpMessage")
    fun sendHelpMessage(
        @Body request: HelpRequest,
    ): Observable<StatusDTO>


    @GET("SelfCare/GetIncidentType")
    fun getIncidentType(): Observable<List<IncidentTypeDTO>>

    @GET("SelfCare/GetTermConditions")
    fun getTerms(): Observable<TermsDTO>

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
        @Query("Sendermsisdn") msisdn: String?,
        @Query("receivermsisdn") receiverMsisdn: String?,
        @Query("voucherNumber") voucherNumber: String?,
    ): Observable<SMSCountDTO>

    @POST("SelfCare/CreditTransfer")
    fun creditTransfer(
        @Query("Sendermsisdn") msisdn: String?,
        @Query("receivermsisdn") receiverMsisdn: String?,
        @Query("amount") amount: String?,
    ): Observable<SMSCountDTO>

    @POST("SelfCare/RefreshToken")
    fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Observable<LoginDTO>

    @GET("SelfCare/GetProfileInfo")
    fun getProfile(): Observable<UserDTO>


    @GET("SelfCare/GetSubAccount")
    fun getSubAccount(): Observable<SubAccountDTO>

    @GET("SelfCare/GetAccountBalance")
    fun getAccountBalance(
        @Query("SubMsisdn") msisdn: String?,
    ): Observable<AccountBalanceDTO>


    @GET("SelfCare/GetFreeNumber")
    fun getFreeNumber(
        @Query("Searchmsisdn") Searchmsisdn: String?
    ): Observable<List<String>>

    @POST("SelfCare/BookingNumber")
    fun bookNumber(
        @Query("freeNumber") freeNumber: String?
    ): Observable<StatusDTO>

    @GET("SelfCare/GetBundlesCategories")
    fun getBundlesCategories(): Observable<List<BundleCategoriesDTO>>

    @GET("SelfCare/GetBundlesByCategories/{categoriesid}")
    fun getBundlesByCategories(
        @Path("categoriesid") categoryId: String?,
        @Query("searchBundle") searchBundle: String?
    ): Observable<List<BundlesDTO>>

    @GET("SelfCare/GetBundlesDetails/{BundleId}")
    fun getBundleDetails(
        @Path("BundleId") BundleId: String
    ): Observable<BundleInfo>

    @POST("SelfCare/ActivateBundles")
    fun activateBundle(
        @Body request: ActivateBundleRequest,
    ): Observable<StatusDTO>


    @GET("SelfCare/DataCalculatorCriteria")
    fun getDataCalculator(
    ): Observable<DataCalculatorDTO>

    @GET("SelfCare/GetMyBundlesandServices")
    fun getMyBundlesAndServices(
        @Query("submsisdn") msisdn: String?,
    ): Observable<List<MyBundlesAndServices>>

    @GET("SelfCare/GetMyServices")
    fun getMyServices(
        @Query("submsisdn") msisdn: String?,
    ): Observable<List<ServicesDTO>>

    @GET("SelfCare/GetVasServices")
    fun getVasServices(
        @Query("submsisdn") msisdn: String?,
    ): Observable<List<ServicesDTO>>


    @DELETE("SelfCare/DeleteSubAccount/{subMsisdn}")
    fun deleteSubAccount(
        @Path("subMsisdn") msisdn: String?,
    ): Observable<SubAccountDTO>


    @POST("SelfCare/AddSubAccount")
    fun addSubAccount(
        @Query("verificationToken") verificationToken: String?,
    ): Observable<SubAccountDTO>


    @GET("SelfCare/GetImagesUrls/{imagetype}/{pageName}")
    fun getImagesURls(
        @Path("imagetype") imageType: String?,
        @Path("pageName") pageName: String?,
    ): Observable<List<String>>


    @GET("SelfCare/ServiceSubscribe")
    fun serviceSubscribe(
        @Query("submsisdn") msisdn: String?,
        @Query("sname") sname : String?,
    ): Observable<StatusDTO>

    @GET("SelfCare/ServiceUnSubscribe")
    fun serviceUnSubscribe(
        @Query("submsisdn") msisdn: String?,
        @Query("sname") sname : String?,
    ): Observable<StatusDTO>

    @POST("SelfCare/SetPlayerId")
    fun setUserPush(
        @Body request: PlayerPushRequest,
    ): Observable<Unit>

    @GET("SelfCare/GetVasServices/{sname}")
    fun getVasServiceDetails(
        @Path("sname") sname: String?
    ): Observable<ServicesDTO>

}