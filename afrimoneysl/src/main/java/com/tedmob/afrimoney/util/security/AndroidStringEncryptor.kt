package com.tedmob.afrimoney.util.security

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidStringEncryptor
@Inject constructor() : StringEncryptor {

    companion object {
        private val ALGO = "AES"
        private val TRANS = "AES/CBC/PKCS5Padding"
        private val PASS = ";97\$6K9hm,!HxN/T"
        private val IV = "t0dmo_999@999---"
    }


    override fun encrypt(str: String): String {
        return str.takeUnless { it.isEmpty() }?.let {

            val cipher = Cipher.getInstance(TRANS)
            val key = PASS.toRawByteArray()
                .apply { fixKey(this) }
            val keySpec = SecretKeySpec(key, ALGO)
            val iv = formIV(IV, 16)
            val ivSpec = IvParameterSpec(iv)

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted = cipher.doFinal(it.toRawByteArray())

            (iv + encrypted).base64Encode()

        }.orEmpty()
    }

    override fun decrypt(str: String): String {
        return str.takeUnless { it.isEmpty() }?.let {

            val cipher = Cipher.getInstance(TRANS)
            val key = PASS.toRawByteArray()
                .apply { fixKey(this) }
            val keySpec = SecretKeySpec(key, ALGO)

            val encryptedBytes = it.base64Decode()
            val iv = encryptedBytes.sliceArray(0 until 16)
            val ivSpec = IvParameterSpec(iv)

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decrypted = cipher.doFinal(encryptedBytes.sliceArray(16 until encryptedBytes.size))

            decrypted.toRawString()

        }.orEmpty()
    }

    private inline fun formIV(iv: String, size: Int): ByteArray {
        val bytes = ByteArray(size)
        iv.toByteArray().take(16).toByteArray().copyInto(bytes)
        return bytes
    }

    private inline fun fixKey(key: ByteArray): ByteArray {
        return if (key.size == 16) {
            val newKey = ByteArray(24)
            System.arraycopy(key, 0, newKey, 0, 16)
            System.arraycopy(key, 0, newKey, 16, 8)
            newKey
        } else {
            key
        }
    }
}