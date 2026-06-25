package com.example.house_rental_app.repository

import com.example.house_rental_app.dao.UserDao
import com.example.house_rental_app.entity.UserEntity

class OfflineUserRepository(override val userDao: UserDao): UserRepository{
    override suspend fun registerUser(user: UserEntity) = userDao.registerUser(user)

    override suspend fun userLogin(email: String, password: String): Int = userDao.userLogin(email, password)  // ИЗМЕНИТЕ

    override suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    override suspend fun fetchUserById(userId: Int): UserEntity {
        return userDao.fetchUserById(userId)
    }
}
