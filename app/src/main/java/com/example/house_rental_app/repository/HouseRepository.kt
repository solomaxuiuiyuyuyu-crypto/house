package com.example.house_rental_app.repository

import com.example.house_rental_app.dao.HouseDao
import com.example.house_rental_app.entity.HouseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface HouseRepository {
    val houseDao: HouseDao

    suspend fun addHouse(house: HouseEntity) {
        withContext(Dispatchers.IO){
            houseDao.addHouse(house)
        }
    }

    suspend fun deleteHouse(house: HouseEntity) {
        withContext(Dispatchers.IO){
            houseDao.deleteHouse(house)
        }
    }

    suspend fun viewAllHousesBasedOnOwnerID(userId: Int): Flow<List<HouseEntity>> {
        return withContext(Dispatchers.IO){
            houseDao.viewAllHousesBasedOnOwnerID(userId)
        }
    }

    suspend fun editHouse(house: HouseEntity) {
        withContext(Dispatchers.IO){
            houseDao.editHouse(house)
        }
    }

    suspend fun viewAllHouses(): Flow<List<HouseEntity>>{
        return withContext(Dispatchers.IO){
            houseDao.viewAllHouses()
        }
    }

    suspend fun getHouseById(houseId: Int): HouseEntity {
        return withContext(Dispatchers.IO) {
            houseDao.getHouseById(houseId)
        }
    }

    suspend fun getHouseByKey(houseKey: String): HouseEntity {
        val numeric = houseKey.toIntOrNull() ?: throw IllegalArgumentException("House key is not numeric")
        return getHouseById(numeric)
    }
}
