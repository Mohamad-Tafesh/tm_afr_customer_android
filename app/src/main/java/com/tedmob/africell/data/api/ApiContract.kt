package com.tedmob.africell.data.api

object ApiContract {
    const val BASE_URL = "https://selfcareapp.africell.ug/"

    object Params {
        const val NO_TOKEN_TAG = "tag__do_not_send_token_header"
        const val PHONE_NUMBER = "111"
        const val SMS_FREE_MAX = 5
        const val NEW_USER_TYPE = 0
        const val FORGOT_PASSWORD_TYPE = 1
        const val SUB_ACCOUNT_TYPE = 2
        const val PREPAID = "Prepaid"
        const val BANNERS = "banners"
        const val SLIDERS = "sliders"
    }

    object ImagePageName {
        const val REPORT_INCIDENT = "report_incident"
        const val CUSTOMER_CARE = "customer_care"
        const val VAS_SERVICES = "vas_services"
        const val BUNDLES_SERVICES = "bundles_services"
        const val SERVICES = "services"
        const val HOME_PAGE = "home"
        const val FORGOT_PASSWORD = "forgot_password"
        const val SIGN_UP = "sign_up"
        const val ACCOUNT_INFO = "account_info"
        const val PROFILE = "profile"
        const val CREDIT_TRANSFER = "credit_transfer"
        const val SETTINGS = "settings"
    }

}