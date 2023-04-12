package com.tedmob.afrimoney.util.phone

import androidx.collection.LruCache
import androidx.collection.lruCache
import com.tedmob.afrimoney.data.entity.Country
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.util.*


class PhoneNumberHelper(
    private val useCache: Boolean,
    private val phoneNumberUtil: PhoneNumberUtil
) {

    companion object {
        val DEFAULT_TYPE = Type.FIXED_LINE_OR_MOBILE
        val DEFAULT_FORMAT = PhoneNumberUtil.PhoneNumberFormat.E164
    }

    object Type {
        const val ALL = 0
        const val FIXED_LINE_OR_MOBILE = 1
        const val MOBILE_OR_UNKNOWN = 2
        const val FIXED_LINE_OR_UNKNOWN = 3
        const val MOBILE = 4
        const val FIXED_LINE = 5
    }


    private data class PhoneValidationRequest(
        val countryCode: String?,
        val nationalNumber: String,
        val type: Int,
        val format: PhoneNumberUtil.PhoneNumberFormat
    )

    private data class PhoneValidationResult(
        val isSuccess: Boolean,
        val formattedNumber: String?
    )

    private val cache: LruCache<PhoneValidationRequest, PhoneValidationResult> = lruCache(
        maxSize = 5,
        create = {
            val formattedNumber = formatNumberIfValid(it.countryCode, it.nationalNumber, it.type, it.format)

            PhoneValidationResult(formattedNumber != null, formattedNumber)
        }
    )


    private fun formatNumberIfValid(
        countryCode: String? = null,
        number: String,
        type: Int = DEFAULT_TYPE,
        format: PhoneNumberUtil.PhoneNumberFormat = DEFAULT_FORMAT
    ): String? {

        var numberTemp = number

        val isoCode: String?
        val phoneNumber: Phonenumber.PhoneNumber
        val isNumberValid: Boolean
        val phoneType: PhoneNumberUtil.PhoneNumberType

        try {

            isoCode = if (countryCode == null || countryCode == "") null
            else phoneNumberUtil.getRegionCodeForCountryCode(
                Integer.parseInt(
                    if (countryCode.startsWith("+")) countryCode.substring(1) else countryCode
                )
            )

            if (!numberTemp.startsWith("+") && isoCode == null) {
                numberTemp = "+$numberTemp"
            }

            phoneNumber = phoneNumberUtil.parse(numberTemp, isoCode)
            isNumberValid = phoneNumberUtil.isValidNumber(phoneNumber)
            phoneType = phoneNumberUtil.getNumberType(phoneNumber)

        } catch (e: NumberParseException) {
            return null
        } catch (e: NumberFormatException) {
            return null
        }

        val isNeededType = when (type) {
            Type.ALL -> true

            Type.FIXED_LINE_OR_MOBILE -> phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE
                    || phoneType == PhoneNumberUtil.PhoneNumberType.MOBILE
                    || phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE

            Type.MOBILE_OR_UNKNOWN -> phoneType == PhoneNumberUtil.PhoneNumberType.MOBILE
                    || phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE

            Type.FIXED_LINE_OR_UNKNOWN -> phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE
                    || phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE

            Type.MOBILE -> phoneType == PhoneNumberUtil.PhoneNumberType.MOBILE

            Type.FIXED_LINE -> phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE

            else -> false
        }

        return if (isNumberValid && isNeededType) phoneNumberUtil.format(
            phoneNumber,
            format
        ) else null
    }


    fun getFormattedIfValid(
        countryCode: String? = null,
        number: String,
        type: Int = DEFAULT_TYPE,
        format: PhoneNumberUtil.PhoneNumberFormat = DEFAULT_FORMAT
    ): String? =
        if (useCache) {
            cache.get(PhoneValidationRequest(countryCode, number, type, format))
                ?.formattedNumber
        } else {
            formatNumberIfValid(countryCode, number, type, format)
        }

    fun isValidNumber(
        countryCode: String? = null,
        number: String,
        type: Int = DEFAULT_TYPE,
        format: PhoneNumberUtil.PhoneNumberFormat = DEFAULT_FORMAT
    ): Boolean =
        if (useCache) {
            cache.get(PhoneValidationRequest(countryCode, number, type, format))
                ?.isSuccess == true
        } else {
            formatNumberIfValid(countryCode, number, type, format) != null
        }

    //fixme does not work for NATIONAL format (because it does not begin with a '+') and for "00" prefix
    fun getCodeAndNumber(phoneNumberStr: String): Pair<String, String>? {
        var phoneNumber: Phonenumber.PhoneNumber
        var countryCode: String
        var phoneNumberNoCode: String

        return try {
            //Assume the number begins with a +, and parse it using the library.
            phoneNumber = phoneNumberUtil.parse(phoneNumberStr, "")

            countryCode = phoneNumber.countryCode.toString()
            phoneNumberNoCode = phoneNumber.nationalNumber.toString()

            Pair(countryCode, phoneNumberNoCode)
        } catch (e: NumberParseException) {
            //Try again by prefixing "+" if possible
            if (!phoneNumberStr.startsWith("+")) {
                try {
                    phoneNumber = phoneNumberUtil.parse("+${phoneNumberStr}", "")

                    countryCode = phoneNumber.countryCode.toString()
                    phoneNumberNoCode = phoneNumber.nationalNumber.toString()

                    Pair(countryCode, phoneNumberNoCode)
                } catch (e: NumberParseException) {
                    null
                }
            } else {
                null
            }
        }
    }

    //fixme does not work for NATIONAL format (because it does not begin with a '+') and for "00" prefix
    fun getCodeAndNumber(phoneNumberStr: String, countryCode: String): Pair<String, String>? {
        var phoneNumber: Phonenumber.PhoneNumber
        var actualCountryCode: String
        var phoneNumberNoCode: String
        val noPrefixCountryCode = countryCode.removePrefix("+")

        return try {
            //Assume the number begins with a +, and parse it using the library.
            phoneNumber = phoneNumberUtil.parse(phoneNumberStr, noPrefixCountryCode)
            actualCountryCode = phoneNumber.countryCode.toString()
            phoneNumberNoCode = phoneNumber.nationalNumber.toString()

            Pair(actualCountryCode, phoneNumberNoCode)
        } catch (e: NumberParseException) {
            //Try again by concatenating and prefixing "+" if possible
            if (!phoneNumberStr.startsWith("+")) {
                try {
                    phoneNumber = phoneNumberUtil.parse("+${noPrefixCountryCode}${phoneNumberStr}", noPrefixCountryCode)
                    actualCountryCode = phoneNumber.countryCode.toString()
                    phoneNumberNoCode = phoneNumber.nationalNumber.toString()

                    Pair(actualCountryCode, phoneNumberNoCode)
                } catch (e: NumberParseException) {
                    null
                }
            } else {
                null
            }
        }
    }

    fun getCountriesAvailable(): List<Country> {
        val countriesMap: MutableMap<String, String> = mutableMapOf()

        return phoneNumberUtil.supportedCallingCodes.flatMap { code ->
            val regions = phoneNumberUtil.getRegionCodesForCountryCode(code)
            regions.map {
                val country =
                    countriesMap[it] ?: run {
                        val value = Locale("en", it).displayCountry
                        countriesMap[it] = value
                        value
                    }

                Country(it, country, "+$code")
            }
                .distinctBy { it.phonecode }
        }
    }
}