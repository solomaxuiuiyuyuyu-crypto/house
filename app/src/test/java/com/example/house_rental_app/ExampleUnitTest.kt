package com.example.house_rental_app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.house_rental_app.dao.UserDao
import com.example.house_rental_app.database.UserDatabase
import com.example.house_rental_app.entity.HouseEntity
import com.example.house_rental_app.entity.UserEntity
import com.example.house_rental_app.utils.HashUtil
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppInstrumentedTest {

    private lateinit var db: UserDatabase
    private lateinit var dao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.userDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun hashPassword_returnsNonEmptyHashAndSalt() {
        val (hash, salt) = HashUtil.SHA256Hasher.hashPasswordWithSalt("testPassword123")
        assertTrue(hash.isNotEmpty())
        assertTrue(salt.isNotEmpty())
    }

    @Test
    fun verifyPassword_correctPassword_returnsTrue() {
        val plain = "mySecret"
        val (hash, salt) = HashUtil.SHA256Hasher.hashPasswordWithSalt(plain)
        assertTrue(HashUtil.SHA256Hasher.verifyPassword(plain, hash, salt))
    }

    @Test
    fun verifyPassword_wrongPassword_returnsFalse() {
        val (hash, salt) = HashUtil.SHA256Hasher.hashPasswordWithSalt("correctPassword")
        assertFalse(HashUtil.SHA256Hasher.verifyPassword("wrongPassword", hash, salt))
    }

    @Test
    fun verifyPassword_emptyPassword_returnsFalse() {
        val (hash, salt) = HashUtil.SHA256Hasher.hashPasswordWithSalt("password")
        assertFalse(HashUtil.SHA256Hasher.verifyPassword("", hash, salt))
    }

    @Test
    fun addProperty_invalidPrice_toIntOrNullReturnsNull() {
        val priceInput = "abc"
        assertNull(priceInput.toIntOrNull())
    }

    @Test
    fun houseEntity_validData_createsCorrectly() {
        val house = HouseEntity(
            ownerId = 1,
            price = 15000,
            address = "ул. Ленина 1",
            lease = "6 месяцев"
        )
        assertEquals(15000, house.price)
        assertEquals("ул. Ленина 1", house.address)
    }

    @Test
    fun houseEntity_defaultAvailability_isTrue() {
        val house = HouseEntity()
        assertTrue(house.isAvailable)
    }

    @Test
    fun sessionManager_saveAndGetUserId_returnsCorrectValue() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        SessionManager.saveUserId(context, "user_key_123")
        assertEquals("user_key_123", SessionManager.getUserId(context))
        SessionManager.clearSession(context)
    }
}

