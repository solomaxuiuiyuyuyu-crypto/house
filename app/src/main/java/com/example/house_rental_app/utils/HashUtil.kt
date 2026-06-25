package com.example.house_rental_app.utils

import java.security.MessageDigest
import java.security.SecureRandom
object HashUtil {
    object SimpleHasher {
        fun hashPassword(password: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
        fun verifyPassword(password: String, hash: String): Boolean {
            return hashPassword(password) == hash
        }
    }
    object SHA256Hasher {
        private const val SALT_LENGTH = 16
        fun generateSalt(): String {
            val salt = ByteArray(SALT_LENGTH)
            SecureRandom().nextBytes(salt)
            return salt.joinToString("") { "%02x".format(it) }
        }
        fun hashPassword(password: String, salt: String): String {
            val saltedPassword = password + salt
            val bytes = MessageDigest.getInstance("SHA-256").digest(saltedPassword.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }
        fun hashPasswordWithSalt(password: String): Pair<String, String> {
            val salt = generateSalt()
            val hash = hashPassword(password, salt)
            return Pair(hash, salt)
        }
        fun verifyPassword(password: String, hash: String, salt: String): Boolean {
            val computedHash = hashPassword(password, salt)
            return computedHash == hash
        }
    }
}