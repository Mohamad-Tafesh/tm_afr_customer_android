package com.africell.africell.util.validation

import com.africell.africell.BuildConfig.FLAVOR
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

object PhoneNumberHelper {
    private val DEFAULT_TYPE = Type.FIXED_LINE_OR_MOBILE
    private val DEFAULT_FORMAT = PhoneNumberUtil.PhoneNumberFormat.E164

    object Type {
        const val ALL = 0
        const val FIXED_LINE_OR_MOBILE = 1
        const val MOBILE = 2
        const val FIXED_LINE = 3
        const val UNKNOWN = 11
    }

    fun getFormattedIfValid(
        countryCode: String?,
        number: String,
        type: Int = DEFAULT_TYPE,
        format: PhoneNumberUtil.PhoneNumberFormat = DEFAULT_FORMAT
    ): String? {

        var numberTemp = number

        val phoneNumberUtil = PhoneNumberUtil.getInstance()
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

            Type.MOBILE -> phoneType == PhoneNumberUtil.PhoneNumberType.MOBILE
                    || phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE

            Type.FIXED_LINE -> phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE
                    || phoneType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE

            Type.UNKNOWN -> phoneType == PhoneNumberUtil.PhoneNumberType.UNKNOWN

            else -> false
        }

        return if (isNumberValid && isNeededType)
            phoneNumberUtil.format(phoneNumber, format)
        else if (FLAVOR == "gambia" && (
                    number.startsWith("22040") ||
                            number.startsWith("22041") ||
                            (number.startsWith("40") && countryCode == "220") ||
                            (number.startsWith("40") && countryCode == "+220") ||
                            (number.startsWith("41") && countryCode == "220") ||
                            (number.startsWith("41") && countryCode == "+220")
                    )
        ) {
            phoneNumberUtil.format(phoneNumber, format)
        } else null
    }

    fun isValid(countryCode: String? = null, phoneNumber: String): Boolean {
        return getFormattedIfValid(countryCode, phoneNumber) != null
    }

    fun isValid(countryCode: String?, phoneNumber: String, type: Int): Boolean {
        return getFormattedIfValid(countryCode, phoneNumber, type) != null
    }

    fun isValid(countryCode: String?, phoneNumber: String, format: PhoneNumberUtil.PhoneNumberFormat): Boolean {
        return getFormattedIfValid(countryCode, phoneNumber, format = format) != null
    }

    fun isValid(countryCode: String?, phoneNumber: String, type: Int, format: PhoneNumberUtil.PhoneNumberFormat): Boolean {
        return getFormattedIfValid(countryCode, phoneNumber, type, format) != null
    }

    //fixme does not work for NATIONAL format (because it does not begin with a '+')
    fun getCodeAndNumber(phoneNumberStr: String): Pair<String, String>? {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()

        val phoneNumber: Phonenumber.PhoneNumber
        val countryCode: String
        val phoneNumberNoCode: String

        return try {
            //Assume the number begins with a +, and parse it using the library.
            phoneNumber = phoneNumberUtil.parse(phoneNumberStr, "")

            countryCode = phoneNumber.countryCode.toString()
            phoneNumberNoCode = phoneNumber.nationalNumber.toString()

            Pair(countryCode, phoneNumberNoCode)
        } catch (e: NumberParseException) {
            null
        }
    }
}