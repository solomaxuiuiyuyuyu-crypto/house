package com.example.house_rental_app.repository

import com.example.house_rental_app.dao.UserDao
import com.example.house_rental_app.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UserRepository {

    val userDao: UserDao

    suspend fun registerUser(user: UserEntity){
        withContext(Dispatchers.IO) {
            userDao.registerUser(user)
        }
    }

    suspend fun userLogin(email: String, password: String): Int{  // ИЗМЕНИТЕ на email
        return withContext(Dispatchers.IO) {
            userDao.userLogin(email, password)  // ИЗМЕНИТЕ на email
        }
    }

    suspend fun fetchUserById(userId: Int): UserEntity {
        return withContext(Dispatchers.IO) {
            userDao.fetchUserById(userId)
        }
    }

    suspend fun updateUser(user: UserEntity){
        withContext(Dispatchers.IO) {
            userDao.updateUser(user)
        }
    }
}
