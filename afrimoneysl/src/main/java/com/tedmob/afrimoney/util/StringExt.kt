package com.tedmob.afrimoney.util

fun String.substringMax(maxChars: Int): String {
    return substring(0..Math.min(maxChars, length - 1))
}
