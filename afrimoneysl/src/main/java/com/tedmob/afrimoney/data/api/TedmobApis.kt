package com.tedmob.afrimoney.data.api

import com.fasterxml.uuid.Generators
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.tedmob.afrimoney.app.AppSessionNavigator
import com.tedmob.afrimoney.data.api.ApiContract.FETCH_TOKEN
import com.tedmob.afrimoney.data.api.dto.*
import com.tedmob.afrimoney.data.repository.domain.SessionRepository
import com.tedmob.afrimoney.exception.AppException
import com.tedmob.afrimoney.exception.AppExceptionFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Suppress("BlockingMethodInNonBlockingContext")
@Singleton
class TedmobApis
@Inject constructor(
    private val api: AfrimoneyRestApi,
    private val session: SessionRepository,
    @Named("Api") private val gson: Gson,
    private val appExceptionFactory: AppExceptionFactory,
    private val appSessionNavigator: AppSessionNavigator,
) {


    suspend fun login(
        username: String,
        pin: String,
    ): LoginDTO {
        return refetchTokenIfNeeded {
            val response = post<CommandContainerDTO<LoginDTO>>(
                "login",
                appHeaders(session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "SLOGINTOKREQ"
                            this["MSISDN"] = username
                            this["PROVIDER"] = "101"
                            this["MPIN"] = pin
                            this["LANGUAGE1"] = "1"
                            //this["BASIC_AUTH"] = Credentials.basic("MobileApp", "zaqwsxasdf1234")
                            this["BASIC_AUTH"] = Credentials.basic("AfriMoney", "zaqwsxasdf12345")
                        }
                    }
                ),
            ).getCommandOrThrow()

            session.refreshToken = response.refreshToken
            session.accessToken = response.token

            response
        }
    }

    suspend fun userInfo(): UserDTO {
        return refreshTokenIfNeeded {
            post<CommandContainerDTO<UserDTO>>(
                "UserEnquiry",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "USERINFOREQ"
                            this["LEVEL"] = "LOW"
                            this["INPUTTYPE"] = "MSISDN"
                            this["IDENTIFICATION"] = session.user?.phoneNumber.orEmpty()
                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }

    suspend fun userInfo(msisdn: String): UserDTO {
        return refetchTokenIfNeeded {
            post<CommandContainerDTO<UserDTO>>(
                "UserEnquiry",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "USERINFOREQ"
                            this["LEVEL"] = "LOW"
                            this["INPUTTYPE"] = "MSISDN"
                            this["IDENTIFICATION"] = msisdn
                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }

    suspend fun checkIfAfrimoneyUser(msisdn: String): Boolean {
        return refetchTokenIfNeeded {
            post<CommandContainerDTO<UserDTO>>(
                "UserEnquiry",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "USERINFOREQ"
                            this["LEVEL"] = "LOW"
                            this["INPUTTYPE"] = "MSISDN"
                            this["IDENTIFICATION"] = msisdn
                        }
                    }
                )
            ).getStatus()
        }
    }

    suspend fun changePin(oldpin: String, newpin: String): ChangePinDTO {
        return refreshTokenIfNeeded {
            post<CommandContainerDTO<ChangePinDTO>>(
                "ChangeMPIN",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "CCMPNREQ"
                            this["MSISDN"] = session.msisdn
                            this["MPIN"] = oldpin
                            this["NEWMPIN"] = newpin
                            this["CONFIRMMPIN"] = newpin
                            this["LANGUAGE1"] = "1"
                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }


    suspend fun checkDSTV(cardNumber: String): CheckDstvDTO {
        return refreshTokenIfNeeded {
            post<CheckDstvDTO>(
                "DSTVGetDueAmountAndDate",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {

                        this["serviceRequestId"] = ""
                        this["interfaceId"] = "DSTV_DETAILS"
                        this["transactionId"] = ""
                        this["SCNumber"] = cardNumber
                        this["serviceType"] = "GETDAMT"
                        this["customerNumber"] = session.msisdn

                    }
                )
            )
        }
    }


    suspend fun getDSTVPress(): PressDTO {
        return refreshTokenIfNeeded {
            get<PressDTO>(
                "DSTV/Subscriptions",
                appHeaders(session.accessToken),
            )
        }
    }


    suspend fun proceedDSTV(
        language: String,
        type: String,
        month: String,
        cardNb: String
    ): ProceedDstvDTO {
        return refreshTokenIfNeeded {
            val response = post<ProceedDstvDTO>(
                "DSTVGetPackageAmount",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),


                body = gsonBody(
                    buildMap<String, Any> {

                        this["serviceType"] = "GETPAMT"
                        this["PackageTypeandLang"] = language
                        this["SubscriptionType"] = type
                        this["Month"] = month
                        this["serviceRequestId"] = ""
                        this["transactionId"] = ""
                        this["interfaceId"] = "DSTV_AMOUNT"
                        this["SmartCardNumber"] = cardNb
                    }
                )
            )

            if (response.status != "200") {
                throw AppException(
                    (response.status)?.toIntOrNull() ?: 0,
                    (response.message).orEmpty(),
                    response.message,
                )
            }
            response
        }
    }

    suspend fun submitRenewDSTV(
        cardNumber: String,
        months: String,
        amount: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["currency"] = "101"
                        this["transactionMode"] = ""
                        this["initiator"] = "transactor"
                        this["language"] = "en"
                        this["billDetails"] = buildMap {
                            this["billNumber"] = ""
                            this["billAccountNumber"] = cardNumber
                        }
                        this["remarks"] = ""
                        this["transactionAmount"] = "201"
                        this["serviceCode"] = "BILLPAY"
                        this["bearerCode"] = "USSD"
                        this["receiver"] = buildMap {
                            this["idType"] = "billerCode"
                            this["idValue"] = "DSTV"
                        }
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = "ACSSE36"
                            this["pin"] = pin
                            this["priceUSDDstv"] = amount
                            this["invoiceMonthNo"] = months


                        }
                    }
                )
            )

            response
        }
    }


    suspend fun powergen(
        accNb: String,
        amount: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "BILLPAY"
                        this["bearerCode"] = "USSD"
                        this["receiver"] = buildMap {
                            this["idType"] = "billerCode"
                            this["productId"] = "12"
                            this["idValue"] = "PWRGEN"
                        }
                        this["billDetails"] = buildMap {
                            this["billNumber"] = ""
                            this["billAccountNumber"] = accNb

                        }
                        this["initiator"] = "transactor"
                        this["transactionMode"] = ""
                        this["transactionAmount"] = amount
                        this["currency"] = "101"
                        this["remarks"] = ""
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = ""
                            this["invoiceMonthNo"] = ""
                            this["priceUSDDstv"] = ""


                        }
                    }
                )
            )

            response
        }
    }


    suspend fun edsa(
        accNb: String,
        amount: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "BILLPAY"
                        this["bearerCode"] = "USSD"
                        this["receiver"] = buildMap {
                            this["idType"] = "billerCode"
                            this["idValue"] = "EDSA"
                        }
                        this["billDetails"] = buildMap {
                            this["billNumber"] = ""
                            this["billAccountNumber"] = accNb

                        }
                        this["initiator"] = "transactor"
                        this["transactionMode"] = ""
                        this["transactionAmount"] = amount
                        this["currency"] = "101"
                        this["remarks"] = ""
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = ""
                            this["invoiceMonthNo"] = ""
                            this["priceUSDDstv"] = ""


                        }
                    }
                )
            )

            response
        }
    }

    suspend fun riacad(
        accNb: String,
        amount: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "BILLPAY"
                        this["bearerCode"] = "USSD"
                        this["receiver"] = buildMap {
                            this["idType"] = "billerCode"
                            this["idValue"] = "RIACAD"
                        }
                        this["billDetails"] = buildMap {
                            this["billNumber"] = ""
                            this["billAccountNumber"] = accNb

                        }
                        this["initiator"] = "transactor"
                        this["transactionMode"] = ""
                        this["transactionAmount"] = amount
                        this["currency"] = "101"
                        this["remarks"] = ""
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = ""
                            this["invoiceMonthNo"] = ""
                            this["priceUSDDstv"] = ""


                        }
                    }
                )
            )

            response
        }
    }

    suspend fun mercury(
        accNb: String,
        amount: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "BILLPAY"
                        this["bearerCode"] = "USSD"
                        this["receiver"] = buildMap {
                            this["idType"] = "billerCode"
                            this["idValue"] = "Mercury2"
                        }
                        this["billDetails"] = buildMap {
                            this["billNumber"] = ""
                            this["billAccountNumber"] = accNb

                        }
                        this["initiator"] = "transactor"
                        this["transactionMode"] = ""
                        this["transactionAmount"] = amount
                        this["currency"] = "101"
                        this["remarks"] = ""
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = ""
                            this["invoiceMonthNo"] = ""
                            this["priceUSDDstv"] = ""


                        }
                    }
                )
            )

            response
        }
    }

    suspend fun feesWaec(
    ): FeesWaecDTO {
        return refreshTokenIfNeeded {
            val response = get<FeesWaecDTO>(
                "getWAECFee",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
            )
            response
        }
    }

    suspend fun Waec(
        pin: String,
        amount: String,
        type: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "BILLPAY"
                        this["bearerCode"] = "USSD"
                        this["receiver"] = buildMap {
                            this["idType"] = "billerCode"
                            this["idValue"] = "WAECBP"
                        }
                        this["billDetails"] = buildMap {
                            this["billNumber"] = ""
                            this["billAccountNumber"] = type

                        }
                        this["initiator"] = "transactor"
                        this["transactionMode"] = ""
                        this["transactionAmount"] = amount
                        this["currency"] = "101"
                        this["remarks"] = "WAEC BILLPAY"
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = ""
                            this["invoiceMonthNo"] = ""
                            this["priceUSDDstv"] = ""


                        }
                    }
                )
            )

            response
        }
    }

    suspend fun registrationWaec(
        accNb: String,
        amount: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "BILLPAY"
                        this["bearerCode"] = "USSD"
                        this["receiver"] = buildMap {
                            this["idType"] = "billerCode"
                            this["idValue"] = "WAECBP"
                        }
                        this["billDetails"] = buildMap {
                            this["billNumber"] = ""
                            this["billAccountNumber"] = accNb

                        }
                        this["initiator"] = "transactor"
                        this["transactionMode"] = ""
                        this["transactionAmount"] = amount
                        this["currency"] = "101"
                        this["remarks"] = "WAEC BILLPAY"
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = ""
                            this["invoiceMonthNo"] = ""
                            this["priceUSDDstv"] = ""


                        }
                    }
                )
            )

            response
        }
    }


    suspend fun postpaid(
        number: String,
        transactionAmount: String,
        pin: String,
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "otherRecharge",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "CTMMOREQ"
                        this["initiator"] = "transactor"
                        this["bearerCode"] = "USSD"
                        this["currency"] = "101"
                        this["language"] = "en"
                        this["custRefNo"] = number
                        this["transactionAmount"] = transactionAmount
                        this["receiver"] = buildMap {
                            this["idValue"] = "OP2005100940"
                            this["idType"] = "operatorId"
                        }
                        this["rechargeReceiver"] = buildMap {
                            this["idValue"] = number
                            this["idType"] = "mobileNumber"
                        }
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["mpin"] = pin
                            this["bundle"] = ""
                            this["idValue"] = session.msisdn
                            this["productId"] = "12"


                        }
                    }
                )
            )

            response
        }
    }


    suspend fun getBankAccounts(): BankDTO {
        return refreshTokenIfNeeded {
            val response = post<CommandContainerDTO<BankDTO>>(
                "getUserBankAccounts",
                appHeaders(session.accessToken) {
                    this["token"] = session.accessToken
                },
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "SUBBKRQ"
                            this["MSISDN"] = session.msisdn
                            this["PROVIDER"] = 101
                            this["BANKID"] = ""
                            this["BLOCKSMS"] = "BOTH"
                            this["LANGUAGE1"] = "1"
                        }
                    }
                )
            )

            if (response.command.status == "006671") {
                response.command
            } else {
                response.getCommandOrThrow()
            }
        }
    }
    /*
        suspend fun airtimeServices(
        ): AfricellServicesDTO {
            return refreshTokenIfNeeded {
                val response = get<AfricellServicesDTO>(
                    "AirtimeLookUp",
                    appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                )
                response
            }
        }
    */

    suspend fun getAvailBank(): AvailBankDTO {
        return refreshTokenIfNeeded {
            val response = get<AvailBankDTO>(
                "getAvailBank",
                appHeaders(session.accessToken) { this["token"] = session.accessToken },
            )
            response
        }
    }

    suspend fun getFeesBankToWallet(
        bankId: String,
        bankNumber: String,
        amount: String
    ): GetFeesDTO {
        return refreshTokenIfNeeded {
            val response = post<GetFeesDTO>(
                "GetFees",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),


                body = gsonBody(
                    buildMap<String, Any> {
                        this["requestedServiceCode"] = "CBWREQ"
                        this["transactionAmount"] = amount
                        this["initiator"] = "transactor"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = ""
                        this["receiver"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                        }
                        this["transactor"] = buildMap {
                            this["bankId"] = bankId
                            this["productId"] = "12"
                            this["idType"] = "mobileNumber"
                            this["idValue"] = session.msisdn
                            this["bankAccountNumber"] = bankNumber

                        }
                    }
                )
            )

            throwIfInvalid(response.status, response.errors)
            response
        }
    }


    suspend fun BankToWallet(
        amount: String,
        bankNumber: String,
        bankID: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BankToWallet",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["currency"] = "101"
                        this["remarks"] = "BankToWallet"
                        this["receiver"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                        }
                        this["serviceCode"] = "CBWREQ"
                        this["transactionMode"] = "transactionMode"
                        this["transactionAmount"] = amount
                        this["initiator"] = "transactor"
                        this["bearerCode"] = "USSD"
                        this["language"] = ""
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["tpin"] = ""
                            this["bankAccountNumber"] = bankNumber
                            this["bankId"] = bankID


                        }
                    }
                )
            )

            response
        }
    }


    suspend fun getFeesWalletToBank(
        bankId: String,
        bankNumber: String,
        amount: String
    ): GetFeesDTO {
        return refreshTokenIfNeeded {
            val response = post<GetFeesDTO>(
                "GetFees",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),


                body = gsonBody(
                    buildMap<String, Any> {
                        this["requestedServiceCode"] = "CWBREQ"
                        this["transactionAmount"] = amount
                        this["initiator"] = "transactor"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = ""
                        this["receiver"] = buildMap {
                            this["bankId"] = bankId
                            this["productId"] = "12"
                            this["idType"] = "mobileNumber"
                            this["idValue"] = session.msisdn
                            this["bankAccountNumber"] = bankNumber

                        }
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn

                        }
                    }
                )
            )

            throwIfInvalid(response.status, response.errors)
            response
        }
    }

    suspend fun WalletToBank(
        amount: String,
        bankNumber: String,
        bankID: String,
        pin: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "WalletToBank",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {

                        this["requestedServiceCode"] = "CWBREQ"
                        this["receiver"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["tpin"] = ""
                            this["bankAccountNumber"] = bankNumber
                            this["bankId"] = bankID
                        }
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["initiator"] = "transactor"
                        this["language"] = ""
                        this["transactionAmount"] = amount
                        this["transactor"] = buildMap {

                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin

                        }
                    }
                )
            )

            response
        }
    }


    suspend fun getBalanceEnquiry(
        bankId: String,
        aaccNb: String,
        pin: String
    ): BankBalanceEnquiryDTO {
        return refetchTokenIfNeeded {
            val response = post<BankBalanceEnquiryDTO>(
                "BankBalanceEnquiry",
                appHeaders(session.deviceToken),
                body = gsonBody(buildMap {
                    this["COMMAND"] = buildMap {
                        this["BANKID"] = bankId
                        this["PAYID"] = "12"
                        this["TYPE"] = "CBALREQ"
                        this["MPIN"] = pin
                        this["LANGUAGE1"] = "1"
                        this["PROVIDER"] = "101"
                        this["ACCNO"] = aaccNb
                        this["MSISDN"] = session.msisdn
                    }
                }
                ),
            )

            response

        }
    }


    suspend fun pendingTransactions(
        pin: String,
        service: String
    ): PendingTransactionsDTO {
        return refreshTokenIfNeeded {

            val response = post<PendingTransactionsDTO>(
                "PendingTransaction",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] =
                            buildMap {
                                this["TYPE"] = "CSMTPREQ"
                                this["MSISDN"] = session.msisdn
                                this["PROVIDER"] = "101"
                                this["PAYID"] = "12"
                                this["TXNID"] = "Y"
                                this["LANGUAGE1"] = "1"
                                this["TXNMODE"] = ""
                                this["BLOCKSMS"] = "BOTH"
                                this["NOOFTXNREQ"] = "10"
                                this["SERVICE"] = service
                                this["MPIN"] = pin


                            }
                    }
                )
            )
            response


        }
    }


    suspend fun confirmCashOutPendingTransaction(
        pin: String,
        SvId: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "ResumeCashOut",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["approvalRequestId"] = "null"
                        this["resumeServiceRequestId"] = SvId
                        this["mfsTenantId"] = "mfsPrimaryTenant"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["party"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                        }

                    }
                )
            )

            response
        }
    }

    suspend fun confirmMerchantPendingTransaction(
        pin: String,
        SvId: String
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "ResumeMerchantPayment",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["resumeServiceRequestId"] = SvId
                        this["mfsTenantId"] = "mfsPrimaryTenant"
                        this["currency"] = "101"
                        this["firstTransactionId"] = ""
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["party"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                        }

                    }
                )
            )

            response
        }
    }

    suspend fun getFeesCashOut(msisdn: String, amount: String, isAfrimoneyUser: Boolean = false): GetFeesDTO {
        return refreshTokenIfNeeded {
            val response = post<GetFeesDTO>(
                "GetFees",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        if (isAfrimoneyUser) this["requestedServiceCode"] = "P2P" else this["requestedServiceCode"] =
                            "P2PNONREG"
                        this["transactionAmount"] = amount
                        this["initiator"] = "withdrawer"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["receiver"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = msisdn
                        }
                        this["sender"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn


                        }
                    }
                )
            )

            throwIfInvalid(response.status, response.errors)
            response
        }
    }

    suspend fun transferMoney(
        msisdn: String,
        amount: String,
        pin: String,
        isAfrimoneyUser: Boolean = false
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                if (isAfrimoneyUser) "P2P" else "P2PUnregistered",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        if (isAfrimoneyUser) this["serviceCode"] = "P2P" else this["serviceCode"] = "P2PNONREG"
                        this["transactionAmount"] = amount
                        this["initiator"] = "sender"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["externalReferenceId"] = getUUID()
                        this["transactionMode"] = "transactionMode"
                        this["receiver"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = msisdn
                        }
                        this["sender"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["tpin"] = pin
                            this["pin"] = pin
                            this["password"] = pin


                        }
                    }
                )
            )

            response
        }
    }

    suspend fun transferMoneyFromRemittance(
        amount: String,
        pin: String,
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "FTBOA",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "FTBOA"
                        this["bearerCode"] = "USSD"
                        this["initiator"] = "transactor"
                        this["currency"] = "101"
                        this["transactionMode"] = ""
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            //this["productId"] = "73" //TEST
                              this["productId"] = "75"   //LIVE
                              this["ufirstName"] = ""
                              this["ulastName"] = ""
                              this["uaddress"] = ""
                              this["invoiceMonthNo"] = ""
                              this["mpin"] = pin
                              this["priceUSDDstv"] = ""
                              this["tpin"] = pin
                              this["userRole"] = "Customer"
                        }
                        this["receiverProxy"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["userRole"] = "Customer"

                        }

                        this["transactionAmount"] = amount
                        this["source"] = "Mobile app"
                        this["externalReferenceId"] = ""
                        this["remarks"] = ""
                        this["externalReferenceId"] = getUUID()
                    }
                )
            )

            response
        }
    }


    suspend fun getFeesMerchantPayment(merchantcode: String, amount: String): GetFeesDTO {
        return refreshTokenIfNeeded {
            val response = post<GetFeesDTO>(
                "GetFees",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["requestedServiceCode"] = "MERCHPAY"
                        this["transactionAmount"] = amount
                        this["initiator"] = "sender"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["transactionMode"] = "transactionMode"
                        this["transactor"] = buildMap {
                            this["idType"] = "userCode"
                            this["productId"] = "12"
                            this["idValue"] = merchantcode
                        }
                        this["sender"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn

                        }
                    }
                )
            )

            throwIfInvalid(response.status, response.errors)
            response
        }
    }

    suspend fun merchantPayment(
        merchantcode: String,
        amount: String,
        pin: String,
        refId: String?
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "MerchantPayment",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "MERCHPAY"
                        this["transactionAmount"] = amount
                        this["initiator"] = "sender"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["externalReferenceId"] = getUUID()
                        this["remarks"] = refId.takeIf { it!!.isNotBlank() } ?: ""
                        this["transactor"] = buildMap {
                            this["idType"] = "userCode"
                            this["productId"] = "12"
                            this["idValue"] = merchantcode
                        }
                        this["sender"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin


                        }
                    }
                )
            )

            response
        }
    }

    suspend fun getFeesAgentCode(code: String, amount: String): GetFeesDTO {
        return refreshTokenIfNeeded {
            val response = post<GetFeesDTO>(
                "GetFees",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),


                body = gsonBody(
                    buildMap<String, Any> {
                        this["requestedServiceCode"] = "CASHOUT"
                        this["transactionAmount"] = amount
                        this["initiator"] = "withdrawer"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "userCode"
                            this["productId"] = "12"
                            this["idValue"] = code
                        }
                        this["withdrawer"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn


                        }
                    }
                )
            )

            throwIfInvalid(response.status, response.errors)
            response
        }
    }

    suspend fun WithdrawAgentCode(
        code: String,
        amount: String,
        pin: String,
        wallet: String,
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "CashOutbyCode",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "CASHOUT"
                        this["transactionAmount"] = amount
                        this["initiator"] = "withdrawer"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["externalReferenceId"] = getUUID()
                        this["remarks"] = "AgentCashOut"
                        this["transactionMode"] = "transactionMode"
                        this["transactor"] = buildMap {
                            this["idType"] = "userCode"
                            this["productId"] = when (wallet) {
                                "Normal" -> "12"
                                "Bonus" -> "12"
                                //"Remittance"-> "73"
                                "Remittance" -> "75"
                                else -> "12"
                            }
                            this["idValue"] = code
                        }
                        this["withdrawer"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["userRole"] = "CUSTOMER"
                            this["identificationNo"] = ""


                        }
                    }
                )
            )


            response
        }
    }


    suspend fun getFeesAgentPhoneNumber(number: String, amount: String): GetFeesDTO {
        return refreshTokenIfNeeded {
            val response = post<GetFeesDTO>(
                "GetFees",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),


                body = gsonBody(
                    buildMap<String, Any> {
                        this["requestedServiceCode"] = "CASHOUT"
                        this["transactionAmount"] = amount
                        this["initiator"] = "withdrawer"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = number
                        }
                        this["withdrawer"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn


                        }
                    }
                )
            )

            throwIfInvalid(response.status, response.errors)
            response
        }
    }

    suspend fun WithdrawAgentPhoneNumber(
        number: String,
        amount: String,
        pin: String,
        wallet: String,
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "CashOutbyMSISDN",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),

                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "CASHOUT"
                        this["transactionAmount"] = amount
                        this["initiator"] = "withdrawer"
                        this["currency"] = "101"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["externalReferenceId"] = getUUID()
                        this["remarks"] = "AgentCashOut"
                        this["transactionMode"] = "transactionMode"
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = when (wallet) {
                                "Normal" -> "12"
                                "Bonus" -> "12"
                                //"Remittance"-> "73"
                                "Remittance" -> "75"
                                else -> "12"
                            }
                            this["idValue"] = number
                        }
                        this["withdrawer"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["productId"] = "12"
                            this["idValue"] = session.msisdn
                            this["mpin"] = pin
                            this["userRole"] = "CUSTOMER"
                            this["identificationNo"] = ""


                        }
                    }
                )
            )

            response
        }
    }


    suspend fun register(
        mobilenb: String,
        firstName: String,
        lastName: String,
        idNumber: String,
        idType: String,
        dob: String, //20081980
        gender: String,
        street: String,
        city: String,
        district: String,
        imtIdNumber: String,
        imtIdType: String,
    ): RegisterDTO {
        return refetchTokenIfNeeded {
            post<CommandContainerDTO<RegisterDTO>>(
                "SubscriberSelfRegistration",
                appHeaders(session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "CUSTREG"
                            this["PROVIDER"] = "101"
                            this["PAYID"] = "12"
                            this["MSISDN"] = mobilenb
                            this["FNAME"] = firstName
                            this["LNAME"] = lastName
                            this["IDNUMBER"] = idNumber
                            this["IDTYPE"] = idType
                            this["DOB"] = dob
                            this["GENDER"] = gender
                            this["ADDRESS"] = street
                            this["CITY"] = city
                            this["DISTRICT"] = district
                            this["REGTYPEID"] = "NO_KYC"
                            this["LOGINID"] = mobilenb
                            this["LANGUAGE1"] = "1"
                            this["IMTIDTYPE"] = imtIdType
                            this["IMTIDNO"] = imtIdNumber
                        }
                    }
                ),
            ).getCommandOrThrow()
        }
    }

    suspend fun getLastNTransactions(
        number: String, pin: String
    ): LastTransactionResponseDTO {
        return refreshTokenIfNeeded {
            post<CommandContainerDTO<LastTransactionResponseDTO>>(
                "LastNTransaction",
                appHeaders(session.accessToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "CLTREQ"
                            this["MSISDN"] = session.user?.phoneNumber.orEmpty()
                            this["PROVIDER"] = "101"
                            this["PAYID"] = "12"
                            this["MPIN"] = pin
                            this["BLOCKSMS"] = ""
                            this["LANGUAGE1"] = "1"
                            this["SERVICE"] = ""
                            this["TXNMODE"] = ""
                            this["NOOFTXNREQ"] = number
                            this["CELLID"] = "Cellid1234"
                            this["FTXNID"] = "FTxnId345"
                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }


    suspend fun africellServices(
    ): AfricellServicesDTO {
        return refreshTokenIfNeeded {
            val response = get<AfricellServicesDTO>(
                "DataBundles",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
            )

            response
        }
    }


    suspend fun airtimeServices(
    ): AfricellServicesDTO {
        return refreshTokenIfNeeded {
            val response = get<AfricellServicesDTO>(
                "AirtimeLookUp",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
            )
            response
        }
    }

    suspend fun getMeter(meterId: String): ClientNawecDTO {
        return refreshTokenIfNeeded {
            post<CommandContainerDTO<ClientNawecDTO>>(
                "Matontine",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {

                        this["transactionId"] = "0Lilly23"
                        this["serviceType"] = "CGMREQ"
                        this["MeterNo"] = meterId
                        this["interfaceId"] = "getMeter"
                        this["MSISDN"] = session.msisdn

                    }
                )
            ).getCommandOrThrow()
        }
    }

    suspend fun getMeters(
    ): ClientDTO {
        return refreshTokenIfNeeded {
            post(
                "NAWEC/GetMeter",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() }
                    ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "SUBBILREQ"
                            this["PROVIDER"] = "101"
                            this["MPIN"] = ""
                            this["MSISDN"] = session.msisdn

                        }
                    }
                )
            )
        }

    }


    suspend fun getFeesNawec(
        meterNumber: String,
        amount: String,
    ): NawecFeesDTO {
        return refreshTokenIfNeeded {
            val response = post<NawecFeesDTO>(
                "trialCreditVendReq",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),


                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceName"] = "TRIALCREDVEND"
                        this["txnAmount"] = amount
                        this["meterNumber"] = meterNumber
                        this["interfaceId"] = "NAWEC"
                        this["serviceType"] = "TRIALCREDVEND"
                        this["TYPE"] = "TRIALCREDVEND"
                    }
                )
            )

            //throwIfInvalid(response.status, response.errors)
            response
        }
    }


    suspend fun confirmBuyNawec(
        number: String,
        transactionAmount: String,
        pin: String,
        /*     uAddress: String,
             field2: String,*/
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "BILLPAY",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "BILLPAY"
                        this["initiator"] = "transactor"
                        this["bearerCode"] = "USSD"
                        this["currency"] = "101"
                        this["language"] = "1"
                        this["transactionMode"] = ""
                        this["remarks"] = ""
                        this["transactionAmount"] = transactionAmount
                        this["receiver"] = buildMap {
                            this["idValue"] = "NAWEC"
                            this["idType"] = "billerCode"
                        }
                        /*     this["extensibleFields"] = buildMap {
                                 this["field1"] = uAddress
                                 this["field2"] = field2
                             }*/
                        this["billDetails"] = buildMap {
                            this["billAccountNumber"] = number
                            this["billNumber"] = ""
                        }
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["mpin"] = pin
                            this["idValue"] = session.msisdn
                            this["productId"] = "12"
                            this["priceUSDDstv"] = ""
                            this["ufirstName"] = ""
                            this["ulastName"] = ""
                            this["uaddress"] = ""
                            this["invoiceMonthNo"] = ""
                        }
                    }
                )
            )

            response
        }
    }

    suspend fun getCustomerInvoices(meterId: String): CustomerInvoiceDTO {
        return refreshTokenIfNeeded {
            post<CustomerInvoiceDTO>(
                "endeRePrint",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["INTERFACEID"] = "ENDE"
                            this["MSISDN"] = session.msisdn
                            this["TYPE"] = "VALINFOREQ"
                            this["SUBTYPE"] = "REPRINT"
                            this["METNUM"] = meterId
                            this["LANGUAGE1"] = if (session.language == "en") "1" else "4"
                        }
                    }
                )
            )
        }
    }

    suspend fun nawecAddClient(
        meterNumber: String,
        nickname: String,
        pin: String
    ): AddClientDTO {
        return refreshTokenIfNeeded {
            post<CommandContainerSpecialCaseDTO<AddClientDTO>>(
                "NAWEC/RegisterMeter",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "BPREGREQ"
                            this["MSISDN"] = session.msisdn
                            this["PROVIDER"] = "101"
                            this["BPCODE"] = "NAWEC"
                            this["MPIN"] = pin
                            this["BLOCKSMS"] = "NONE"
                            this["TXNMODE"] = ""
                            this["NICK_NAME"] = meterNumber
                            this["PREF1"] = meterNumber
                            this["PREF2"] = ""
                            this["LANGUAGE1"] = "1"
                            this["LANGUAGE2"] = "1"
                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }

    suspend fun deleteCustomer(
        accNb: String,
        nickname: String,
        pin: String
    ): ConfirmNawecDTO {
        return refreshTokenIfNeeded {
            post<ConfirmNawecDTO>(
                "NAWEC/RegisterMeter",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "CBPREGDREQ"
                            this["MSISDN"] = session.msisdn
                            this["PROVIDER"] = "101"
                            this["BPCODE"] = "NAWEC"
                            this["MPIN"] = pin
                            this["BLOCKSMS"] = "NONE"
                            this["TXNMODE"] = ""
                            this["LANGUAGE1"] = "1"
                            this["LANGUAGE2"] = "1"
                            this["NICK_NAME"] = accNb
                            this["PREF1"] = accNb
                            this["PREF2"] = ""
                        }
                    }
                )
            )
        }
    }


    suspend fun bundleSelf(
        remark: String,
        transactionAmount: String,
        pin: String,
        idValue: String,
        idType: String,
        bundle: String,
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "selfRecharge",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["transactionMode"] = "transactionMode"
                        this["remarks"] = remark
                        this["source"] = "mobile_app"
                        this["bearerCode"] = "USSD"
                        this["language"] = "en"
                        this["externalReferenceId"] = getUUID()
                        this["initiator"] = "transactor"
                        this["transactionMode"] = "transactionMode"
                        this["serviceCode"] = "RC"
                        this["currency"] = "101"
                        this["transactionAmount"] = transactionAmount
                        this["receiver"] = buildMap {
                            this["idValue"] = idValue
                            this["idType"] = idType
                            this["productId"] = "12"
                            this["currency"] = "101"
                        }
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["mpin"] = pin
                            this["bundle"] = bundle
                            this["idValue"] = session.msisdn
                            this["productId"] = "12"


                        }
                    }
                )
            )

            response
        }
    }

    suspend fun bundleOther(
        number: String,
        transactionAmount: String,
        pin: String,
        idValue: String,
        idType: String,
        bundle: String,
    ): ConfirmTransferMoneyDTO {
        return refreshTokenIfNeeded {
            val response = post<ConfirmTransferMoneyDTO>(
                "otherRecharge",
                appHeaders(session.accessToken.takeIf { it.isNotBlank() } ?: session.deviceToken),
                body = gsonBody(
                    buildMap<String, Any> {
                        this["serviceCode"] = "CTMMOREQ"
                        this["initiator"] = "transactor"
                        this["bearerCode"] = "USSD"
                        this["currency"] = "101"
                        this["language"] = "en"
                        this["custRefNo"] = number
                        this["transactionAmount"] = transactionAmount
                        this["receiver"] = buildMap {
                            this["idValue"] = idValue
                            this["idType"] = idType
                        }
                        this["rechargeReceiver"] = buildMap {
                            this["idValue"] = number
                            this["idType"] = "mobileNumber"
                        }
                        this["transactor"] = buildMap {
                            this["idType"] = "mobileNumber"
                            this["mpin"] = pin
                            this["bundle"] = bundle
                            this["idValue"] = session.msisdn
                            this["productId"] = "12"


                        }
                    }
                )
            )

            response
        }
    }


    suspend fun getBalance(type: String): BalanceDTO {
        return refreshTokenIfNeeded {
            post<CommandContainerDTO<BalanceDTO>>(
                "BalanceEnquiry",
                appHeaders(session.accessToken) {
                    this["token"] = session.accessToken
                },
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "CBEREQ"
                            this["MSISDN"] = session.msisdn
                            this["PROVIDER"] = "101"
                            this["PAYID"] = type
                            this["LANGUAGE1"] = "1"
                            this["BLOCKSMS"] = "BOTH"
                            this["CELLID"] = ""
                            this["FTXNID"] = ""

                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }

    suspend fun generateOtp(msisdn: String): GenerateOtpDTO {
        return refetchTokenIfNeeded {
            post<CommandContainerDTO<GenerateOtpDTO>>(
                "GenerateOTP",
                appHeaders(session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "GENOTPREQ"
                            this["MSISDN"] = msisdn
                            this["PROVIDER"] = "101"
                            this["SRVREQTYPE"] = "CUSTREG"
                            this["LANGUAGE1"] = "1"
                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }

    suspend fun validateOtp(msisdn: String, otp: String): GenerateOtpDTO {
        return refetchTokenIfNeeded {
            post<CommandContainerDTO<GenerateOtpDTO>>(
                "ValidateOTP",
                appHeaders(session.deviceToken),
                body = gsonBody(
                    buildMap {
                        this["COMMAND"] = buildMap {
                            this["TYPE"] = "VALOTPREQ"
                            this["MSISDN"] = msisdn
                            this["PROVIDER"] = "101"
                            this["SRVREQTYPE"] = "CUSTREG"
                            this["LANGUAGE1"] = "1"
                            this["OTP"] = otp
                            this["PAYID"] = "12"
                            this["USERTYPE"] = "SUBSCRIBER"
                        }
                    }
                )
            ).getCommandOrThrow()
        }
    }


    suspend fun refreshToken() {

        try {
            val response = post<RefreshTokenDTO>(
                "RefreshUserToken",
                body = fields {
                    this["grant_type"] = "refresh_token"
                    this["refresh_token"] = session.refreshToken
                },
            )
            session.accessToken = response.accessToken
            session.refreshToken = response.refreshToken
        } catch (e: Exception) {
            appSessionNavigator.restart()
        }


    }

    private suspend inline fun <reified T> refreshTokenIfNeeded(block: () -> T): T {
        return runCatching(block)
            .recover {
                if (it is CancellationException) throw it

                val exception = appExceptionFactory.make(it)
                if (exception.status == 401) {
                    refreshToken()
                    block()
                } else {
                    throw exception
                }
            }
            .getOrThrow()
    }

    suspend fun fetchToken(): FetchTokenDTO {
        val response = post<FetchTokenDTO>(
            FETCH_TOKEN,
            headers = headers {
                this["Authorization"] =
                    "Basic QWZyaU1vbmV5OnphcXdzeGFzZGYxMjM0NQ=="//Credentials.basic("MobileApp", "zaqwsxasdf1234")
            },
            body = fields {
                this["grant_type"] = "client_credentials"
            },
        )

        session.deviceToken = response.accessToken
        session.accessToken = response.accessToken


        return response
    }

    private suspend inline fun <reified T> refetchTokenIfNeeded(block: () -> T): T {
        return runCatching(block)
            .recover {
                if (it is CancellationException) throw it

                val exception = appExceptionFactory.make(it)
                if (exception.status == 401) {
                    fetchToken()
                    block()
                } else {
                    throw exception
                }
            }
            .getOrThrow()
    }

    private suspend inline fun <reified T> get(
        path: String,
        headers: Headers = appHeaders(),
        params: Params = params { },
    ): T {
        return withContext(Dispatchers.IO) {
            //fixme encryption
            val response = api.get(
                path,
                headers,
                params.getKeyedQueries(),
                params.noKeyValues,
            )
            //fixme decryption
            response.use {
                val reader = gson.newJsonReader(it.charStream())
                val adapter = gson.getAdapter(object : TypeToken<T>() {})

                val result = adapter.read(reader)
                if (reader.peek() != JsonToken.END_DOCUMENT) {
                    throw JsonIOException("JSON document was not fully consumed.")
                }
                result
            }
        }
    }

    private suspend inline fun <reified T> post(
        path: String,
        headers: Headers = appHeaders(),
        params: Params = params { },
        body: Body,
    ): T {
        return withContext(Dispatchers.IO) {
            //fixme encryption
            val request = body.toRequest()
            val response = api.post(
                path,
                headers,
                params.getKeyedQueries(),
                params.noKeyValues,
                request,
            )
            //fixme decryption
            response.use {
                val reader = gson.newJsonReader(it.charStream())
                val adapter = gson.getAdapter(object : TypeToken<T>() {})

                val result = adapter.read(reader)
                if (reader.peek() != JsonToken.END_DOCUMENT) {
                    throw JsonIOException("JSON document was not fully consumed.")
                }
                result
            }
        }
    }

    private fun appHeaders(
        token: String? = null,
        additionalHeadersBlock: Headers.Builder.() -> Unit = {},
    ) = headers {
        if (token != null)
            this["Authorization"] = "Bearer $token"
        else
        // this["Authorization"] = Credentials.basic("MobileApp", "zaqwsxasdf1234")
            this["Authorization"] = Credentials.basic("AfriMoney", "zaqwsxasdf12345")

        additionalHeadersBlock()
    }

    //...


    private inline fun params(builder: Params.() -> Unit): Params = Params().apply(builder)

    private class Params {
        val values: MutableMap<String, Any?> = mutableMapOf()
        var noKeyValues: MutableList<String>? = null

        inline operator fun set(key: String, value: Any?) {
            values[key] = value
        }

        inline operator fun plusAssign(value: String) {
            if (noKeyValues == null) {
                noKeyValues = mutableListOf()
            }
            noKeyValues?.add(value)
        }


        inline fun getKeyedQueries(): Map<String, String> = buildMap {
            this@Params.values.forEach { (key, value) ->
                if (value != null)
                    this[key] = value.toString()
            }
        }
    }


    private inline fun headers(builder: Headers.Builder.() -> Unit): Headers =
        Headers.Builder().apply(builder).build()


    private sealed interface Body {
        fun toRequest(): RequestBody
    }

    private class FormBody : Body {
        val fields: MutableMap<String, Any?> = mutableMapOf()

        inline operator fun set(key: String, value: Any?) {
            fields[key] = value
        }

        override fun toRequest(): RequestBody {
            return okhttp3.FormBody.Builder()
                .also {
                    fields.forEach { (key, value) ->
                        if (value != null) {
                            it.add(key, value.toString())
                        }
                    }
                }
                .build()
        }
    }

    private inline fun fields(block: FormBody.() -> Unit) =
        FormBody().apply(block)

    private class StringBody : Body {
        var value: String = ""
        var contentType: MediaType? = "text/plain".toMediaTypeOrNull()

        override fun toRequest(): RequestBody = value.toRequestBody(contentType)
    }

    private inline fun stringBody(
        value: String,
        contentType: MediaType? = "text/plain".toMediaTypeOrNull()
    ) =
        StringBody().apply {
            this.value = value
            this.contentType = contentType
        }

    private inline fun jsonBody(body: JsonElement) =
        stringBody(body.toString(), "application/json".toMediaTypeOrNull())

    private inline fun <reified T> gsonBody(body: T) =
        stringBody(
            gson.toJson(body, TypeToken.get(T::class.java).type),
            "application/json".toMediaTypeOrNull(),
        )

    private class MultiPartBody : Body {
        //...

        override fun toRequest(): RequestBody {
            TODO("Not yet implemented")
        }
    }


    private fun getUUID() = Generators.timeBasedGenerator().generate().toString()
}