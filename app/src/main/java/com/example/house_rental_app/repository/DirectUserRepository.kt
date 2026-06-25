package com.example.house_rental_app.repository

import android.util.Log
import com.example.house_rental_app.data.FirebaseHelper
import com.example.house_rental_app.entity.UserEntity

class DirectUserRepository {
    private val firebaseHelper = FirebaseHelper()
    private val TAG = "DirectUserRepository"

    suspend fun registerUser(user: UserEntity): Boolean {
        return firebaseHelper.registerUser(user)
    }

    suspend fun loginUser(email: String, password: String): String? {
        return firebaseHelper.loginUser(email, password)
    }

    suspend fun getUserById(userId: String): UserEntity? {
        return firebaseHelper.getUserById(userId)
    }

    suspend fun updateUser(userId: String, user: UserEntity): Boolean {
        return firebaseHelper.updateUser(userId, user)
    }
}