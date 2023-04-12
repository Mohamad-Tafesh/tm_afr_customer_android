package com.tedmob.afrimoney.util.security

import android.util.Base64
import java.io.UnsupportedEncodingException

inline fun String.toRawByteArray(): ByteArray =
    try {
        toByteArray(Charsets.UTF_8)
    } catch (e: UnsupportedEncodingException) {
        toByteArray()
    }

inline fun ByteArray.toRawString(): String = toString(Charsets.UTF_8)

inline fun ByteArray.base64Encode(): String = Base64.encodeToString(this, Base64.NO_WRAP)

inline fun String.base64Decode(): ByteArray = Base64.decode(this, Base64.NO_WRAP)


inline fun <T> T.encryptWith(encryptor: Encryptor<T>): String = try {
    encryptor.encrypt(this)
} catch (e: Exception) {
    e.printStackTrace()
    ""
}
inline fun <T> String.decryptWith(encryptor: Encryptor<T>): T = encryptor.decrypt(this)