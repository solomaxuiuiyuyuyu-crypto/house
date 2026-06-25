package com.example.house_rental_app.data

import android.util.Log
import com.example.house_rental_app.entity.HouseEntity
import com.example.house_rental_app.entity.UserEntity
import com.example.house_rental_app.utils.HashUtil
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseHelper {
    val database = FirebaseDatabase.getInstance().reference  // Сделал public
    private val TAG = "FirebaseHelper"

    // --- Регистрация пользователя ---
    suspend fun registerUser(user: UserEntity): Boolean {
        return try {
            val userId = database.child("users").push().key ?: return false
            val hashedUser = user.copy()
            hashedUser.hashAndSetPassword(user.password)
            val userMap = mapOf(
                "id" to userId,
                "username" to hashedUser.username,
                "password" to hashedUser.password,
                "passwordSalt" to hashedUser.passwordSalt,
                "email" to hashedUser.email,
                "phoneNumber" to hashedUser.phoneNumber,
                "showOnlyEmail" to hashedUser.showOnlyEmail,
                "showOnlyPhone" to hashedUser.showOnlyPhone,
                "createdAt" to (hashedUser.createdAt ?: System.currentTimeMillis().toString()),
                "updatedAt" to (hashedUser.updatedAt ?: System.currentTimeMillis().toString()),
                "passwordMigrated" to true
            )
            database.child("users").child(userId).setValue(userMap).await()
            Log.d(TAG, "User registered with ID: $userId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error registering user: ${e.message}", e)
            false
        }
    }


    // --- Логин пользователя с поддержкой старых паролей ---
    suspend fun loginUser(email: String, password: String): String? {
        return try {
            Log.d(TAG, "Attempting login for email: $email")
            val snapshot = database.child("users").get().await()
            Log.d(TAG, "Total users in DB: ${snapshot.childrenCount}")
            for (userSnapshot in snapshot.children) {
                val userEmail = userSnapshot.child("email").getValue(String::class.java)
                val storedHash = userSnapshot.child("password").getValue(String::class.java)
                val storedSalt = userSnapshot.child("passwordSalt").getValue(String::class.java) ?: ""
                val userId = userSnapshot.key
                val isMigrated = userSnapshot.child("passwordMigrated").getValue(Boolean::class.java) ?: false
                if (userEmail != null && userEmail.equals(email, ignoreCase = true)) {
                    var passwordVerified = false
                    if (isMigrated) {
                        passwordVerified = HashUtil.SHA256Hasher.verifyPassword(password, storedHash ?: "", storedSalt)
                    } else {
                        passwordVerified = storedHash == password
                        if (passwordVerified && userId != null) {
                            Log.d(TAG, "Auto-migrating user: $email")
                            val migratedUser = UserEntity()
                            migratedUser.hashAndSetPassword(password)
                            val updates = mapOf(
                                "password" to migratedUser.password,
                                "passwordSalt" to migratedUser.passwordSalt,
                                "updatedAt" to System.currentTimeMillis().toString(),
                                "passwordMigrated" to true
                            )
                            database.child("users").child(userId).updateChildren(updates).await()
                        }
                    }

                    if (passwordVerified) {
                        Log.d(TAG, "Login successful for userId: $userId")
                        return userId
                    }
                }
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error logging in: ${e.message}", e)
            null
        }
    }

    // --- Получение пользователя по ID ---
    suspend fun getUserById(userId: String): UserEntity? {
        return try {
            val snapshot = database.child("users").child(userId).get().await()
            if (snapshot.exists()) {
                UserEntity(
                    id = 0,
                    username = snapshot.child("username").getValue(String::class.java) ?: "",
                    password = snapshot.child("password").getValue(String::class.java) ?: "",
                    passwordSalt = snapshot.child("passwordSalt").getValue(String::class.java) ?: "",
                    email = snapshot.child("email").getValue(String::class.java) ?: "",
                    phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java) ?: "",
                    showOnlyEmail = snapshot.child("showOnlyEmail").getValue(Boolean::class.java) ?: true,
                    showOnlyPhone = snapshot.child("showOnlyPhone").getValue(Boolean::class.java) ?: true,
                    createdAt = snapshot.child("createdAt").getValue(String::class.java),
                    updatedAt = snapshot.child("updatedAt").getValue(String::class.java)
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user: ${e.message}", e)
            null
        }
    }

    // --- Обновление пользователя ---
    suspend fun updateUser(userId: String, user: UserEntity): Boolean {
        return try {
            val updates = mutableMapOf<String, Any>(
                "username" to user.username,
                "phoneNumber" to user.phoneNumber,
                "showOnlyEmail" to user.showOnlyEmail,
                "showOnlyPhone" to user.showOnlyPhone,
                "updatedAt" to System.currentTimeMillis().toString()
            )

            // Обновляем пароль только если он не пустой
            if (user.password.isNotEmpty()) {
                updates["password"] = user.password
                updates["passwordSalt"] = user.passwordSalt
            }

            database.child("users").child(userId).updateChildren(updates).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user: ${e.message}", e)
            false
        }
    }

    // --- Работа с объявлениями (оставляем как было) ---
    suspend fun addHouse(house: HouseEntity): Boolean {
        return try {
            val houseId = database.child("houses").push().key ?: return false
            val houseMap = mapOf(
                "houseId" to houseId,
                "ownerId" to house.ownerId.toString(),
                "ownerKey" to house.ownerKey,
                "price" to house.price,
                "address" to house.address,
                "images" to house.images,
                "lease" to house.lease,
                "description" to house.description,
                "isAvailable" to house.isAvailable,
                "createdAt" to (house.createdAt ?: System.currentTimeMillis().toString()),
                "updatedAt" to (house.updatedAt ?: System.currentTimeMillis().toString())
            )
            database.child("houses").child(houseId).setValue(houseMap).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error adding house: ${e.message}", e)
            false
        }
    }

    suspend fun getAllHouses(): List<HouseEntity> {
        return try {
            val snapshot = database.child("houses").get().await()
            val houses = mutableListOf<HouseEntity>()
            for (houseSnapshot in snapshot.children) {
                val firebaseKey = houseSnapshot.key.orEmpty()
                val ownerKey = houseSnapshot.child("ownerKey").getValue(String::class.java).orEmpty()
                val house = HouseEntity(
                    houseId = firebaseKey.hashCode(),
                    ownerId = houseSnapshot.child("ownerId").getValue(String::class.java)?.toIntOrNull() ?: 0,
                    firebaseHouseId = firebaseKey,
                    ownerKey = ownerKey,
                    price = houseSnapshot.child("price").getValue(Int::class.java) ?: 0,
                    address = houseSnapshot.child("address").getValue(String::class.java) ?: "",
                    images = houseSnapshot.child("images").getValue(String::class.java) ?: "",
                    lease = houseSnapshot.child("lease").getValue(String::class.java) ?: "",
                    description = houseSnapshot.child("description").getValue(String::class.java) ?: "",
                    isAvailable = houseSnapshot.child("isAvailable").getValue(Boolean::class.java) ?: true,
                    createdAt = houseSnapshot.child("createdAt").getValue(String::class.java),
                    updatedAt = houseSnapshot.child("updatedAt").getValue(String::class.java)
                )
                if (house.isAvailable) {
                    houses.add(house)
                }
            }
            houses
        } catch (e: Exception) {
            Log.e(TAG, "Error getting houses: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getHouseById(houseId: String): HouseEntity? {
        return try {
            val snapshot = database.child("houses").child(houseId).get().await()
            if (snapshot.exists()) {
                val ownerKey = snapshot.child("ownerKey").getValue(String::class.java).orEmpty()
                HouseEntity(
                    houseId = houseId.hashCode(),
                    ownerId = snapshot.child("ownerId").getValue(String::class.java)?.toIntOrNull() ?: 0,
                    firebaseHouseId = snapshot.key.orEmpty(),
                    ownerKey = ownerKey,
                    price = snapshot.child("price").getValue(Int::class.java) ?: 0,
                    address = snapshot.child("address").getValue(String::class.java) ?: "",
                    images = snapshot.child("images").getValue(String::class.java) ?: "",
                    lease = snapshot.child("lease").getValue(String::class.java) ?: "",
                    description = snapshot.child("description").getValue(String::class.java) ?: "",
                    isAvailable = snapshot.child("isAvailable").getValue(Boolean::class.java) ?: true,
                    createdAt = snapshot.child("createdAt").getValue(String::class.java),
                    updatedAt = snapshot.child("updatedAt").getValue(String::class.java)
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting house: ${e.message}", e)
            null
        }
    }

    suspend fun deleteHouse(houseId: String): Boolean {
        return try {
            database.child("houses").child(houseId).removeValue().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting house: ${e.message}", e)
            false
        }
    }

    suspend fun getHousesByOwnerId(ownerId: String): List<HouseEntity> {
        return try {
            val snapshot = database.child("houses").get().await()
            val houses = mutableListOf<HouseEntity>()
            for (houseSnapshot in snapshot.children) {
                val houseOwnerId = houseSnapshot.child("ownerId").getValue(String::class.java)
                if (houseOwnerId == ownerId) {
                    val firebaseKey = houseSnapshot.key.orEmpty()
                    val ownerKey = houseSnapshot.child("ownerKey").getValue(String::class.java).orEmpty()
                    val house = HouseEntity(
                        houseId = firebaseKey.hashCode(),
                        ownerId = houseOwnerId?.toIntOrNull() ?: 0,
                        firebaseHouseId = firebaseKey,
                        ownerKey = ownerKey,
                        price = houseSnapshot.child("price").getValue(Int::class.java) ?: 0,
                        address = houseSnapshot.child("address").getValue(String::class.java) ?: "",
                        images = houseSnapshot.child("images").getValue(String::class.java) ?: "",
                        lease = houseSnapshot.child("lease").getValue(String::class.java) ?: "",
                        description = houseSnapshot.child("description").getValue(String::class.java) ?: "",
                        isAvailable = houseSnapshot.child("isAvailable").getValue(Boolean::class.java) ?: true,
                        createdAt = houseSnapshot.child("createdAt").getValue(String::class.java),
                        updatedAt = houseSnapshot.child("updatedAt").getValue(String::class.java)
                    )
                    houses.add(house)
                }
            }
            houses
        } catch (e: Exception) {
            Log.e(TAG, "Error getting houses by owner: ${e.message}", e)
            emptyList()
        }
    }
}