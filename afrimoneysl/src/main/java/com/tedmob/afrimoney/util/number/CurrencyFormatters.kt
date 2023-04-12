package com.tedmob.afrimoney.util.number

import java.math.BigDecimal
import java.text.DecimalFormat

val defaultCurrency: String = "NLe"


inline fun BigDecimal.formatAmount() =
    DecimalFormat("#,###,##0.##").format(this)

inline fun BigDecimal.formatAmountWith(currency: String) =
    DecimalFormat("${currency.sanitizeForDecimalFormat()}#,###,##0.##").format(this)

inline fun String.sanitizeForDecimalFormat() =
    replace("E", "'E'")


inline fun <reified T> withAmountFormatter(block: DecimalFormat.() -> T): T =
    DecimalFormat("#,###,##0.##").block()

inline fun <reified T> withAmountFormatter(currency: String, block: DecimalFormat.() -> T): T =
    DecimalFormat("${currency.sanitizeForDecimalFormat()}#,###,##0.##").block()