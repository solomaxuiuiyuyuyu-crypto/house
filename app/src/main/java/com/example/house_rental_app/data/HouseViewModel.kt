package com.example.house_rental_app.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.house_rental_app.DatabaseApplication
import com.example.house_rental_app.entity.HouseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HouseViewModel() : ViewModel() {
    private val _allHouses = MutableLiveData<List<HouseEntity>>()
    val allHouses: LiveData<List<HouseEntity>> get() = _allHouses

    private val _viewedHouse = MutableLiveData<HouseEntity?>()
    val viewedHouse: LiveData<HouseEntity?> = _viewedHouse

    private val houseRepository by lazy {
        DatabaseApplication.container.houseRepository
    }

    init {
        refreshHouses()
    }

    fun refreshHouses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Используем Flow из интерфейса
                houseRepository.viewAllHouses().collect { houses ->
                    _allHouses.postValue(houses)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _allHouses.postValue(emptyList())
            }
        }
    }

    // Function to add a new house
    suspend fun addHouse(house: HouseEntity): Boolean {
        return try {
            houseRepository.addHouse(house)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Function to delete a house
    fun deleteHouse(houseId: Int, ownerId: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val house = houseRepository.getHouseById(houseId)
                if (house != null) {
                    houseRepository.deleteHouse(house)
                    // После удаления обновляем список
                    if (ownerId != null) {
                        viewHousesByOwnerId(ownerId)
                    } else {
                        refreshHouses()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Function to retrieve all houses based on owner ID
    fun viewHousesByOwnerId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                houseRepository.viewAllHousesBasedOnOwnerID(userId).collect { houses ->
                    _allHouses.postValue(houses)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _allHouses.postValue(emptyList())
            }
        }
    }

    // Function to edit house details
    fun updateHouse(house: HouseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                houseRepository.editHouse(house)
                refreshHouses()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getHouseById(houseId: Int) {
        viewModelScope.launch {
            try {
                val house = houseRepository.getHouseById(houseId)
                _viewedHouse.postValue(house)
            } catch (e: Exception) {
                e.printStackTrace()
                _viewedHouse.postValue(null)
            }
        }
    }

    fun getHouseByKey(houseKey: String) {
        viewModelScope.launch {
            try {
                val house = houseRepository.getHouseByKey(houseKey)
                _viewedHouse.postValue(house)
            } catch (e: Exception) {
                e.printStackTrace()
                _viewedHouse.postValue(null)
            }
        }
    }
}
