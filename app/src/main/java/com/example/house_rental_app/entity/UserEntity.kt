package com.example.house_rental_app.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.house_rental_app.utils.HashUtil
import com.google.firebase.database.IgnoreExtraProperties

@Entity(tableName = "user_table",
    indices = [Index(value = ["email"], unique = true)]
)
@IgnoreExtraProperties
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var username: String = "",
    var password: String = "",
    var passwordSalt: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var showOnlyEmail: Boolean = true,
    var showOnlyPhone: Boolean = true,
    var createdAt: String? = null,
    var updatedAt: String? = null
) {
    fun hashAndSetPassword(plainPassword: String) {
        val (hash, salt) = HashUtil.SHA256Hasher.hashPasswordWithSalt(plainPassword)
        this.password = hash
        this.passwordSalt = salt
    }

    fun verifyPassword(plainPassword: String): Boolean {
        return HashUtil.SHA256Hasher.verifyPassword(plainPassword, this.password, this.passwordSalt)
    }
}