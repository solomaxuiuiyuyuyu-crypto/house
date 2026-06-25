package com.example.house_rental_app.repository

import android.util.Log
import com.example.house_rental_app.dao.HouseDao
import com.example.house_rental_app.data.FirebaseHelper
import com.example.house_rental_app.entity.HouseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DirectHouseRepository : HouseRepository {
    private val firebaseHelper = FirebaseHelper()
    private val TAG = "DirectHouseRepository"

    override val houseDao: HouseDao
        get() = throw UnsupportedOperationException("DirectHouseRepository doesn't use Room DAO")

    override suspend fun addHouse(house: HouseEntity) {
        firebaseHelper.addHouse(house)
    }

    override suspend fun deleteHouse(house: HouseEntity) {
        firebaseHelper.deleteHouse(house.firebaseHouseId)
    }

    override suspend fun viewAllHousesBasedOnOwnerID(userId: Int): Flow<List<HouseEntity>> {
        return flow {
            val houses = firebaseHelper.getHousesByOwnerId(userId.toString())
            emit(houses)
        }
    }

    override suspend fun editHouse(house: HouseEntity) {
        firebaseHelper.deleteHouse(house.houseId.toString())
        firebaseHelper.addHouse(house)
    }

    override suspend fun viewAllHouses(): Flow<List<HouseEntity>> {
        return flow {
            val houses = firebaseHelper.getAllHouses()
            emit(houses)
        }
    }

    override suspend fun getHouseById(houseId: Int): HouseEntity {
        return firebaseHelper.getHouseById(houseId.toString()) ?: throw Exception("House not found")
    }

    override suspend fun getHouseByKey(houseKey: String): HouseEntity {
        return firebaseHelper.getHouseById(houseKey) ?: throw Exception("House not found")
    }
}