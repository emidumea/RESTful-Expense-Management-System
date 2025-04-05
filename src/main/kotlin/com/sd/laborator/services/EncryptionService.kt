package com.sd.laborator.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Service
class EncryptionService {
    @Value("\${encryption.key}")
    private lateinit var keyString: String

    private lateinit var key: ByteArray
    private lateinit var cipher: Cipher

    @PostConstruct
    fun init() {
        key = keyString.toByteArray()
        cipher = Cipher.getInstance("AES")
    }

    fun encrypt(data: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"))
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
    }

    fun decrypt(data: String): String {
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"))
        return String(cipher.doFinal(Base64.getDecoder().decode(data)))
    }
}