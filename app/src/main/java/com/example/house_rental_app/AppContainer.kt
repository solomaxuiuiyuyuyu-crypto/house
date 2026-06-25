package com.example.house_rental_app

import android.content.Context
import com.example.house_rental_app.repository.DirectHouseRepository
import com.example.house_rental_app.repository.DirectUserRepository
import com.example.house_rental_app.repository.HouseRepository

interface AppContainer {
    val userRepository: DirectUserRepository
    val houseRepository: HouseRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val userRepository: DirectUserRepository by lazy {
        DirectUserRepository()
    }

    override val houseRepository: HouseRepository by lazy {
        DirectHouseRepository()
    }
}