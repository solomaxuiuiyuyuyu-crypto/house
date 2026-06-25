package com.example.house_rental_app

import android.app.Application
import android.util.Log
import com.yandex.mapkit.MapKitFactory



class DatabaseApplication : Application() {
    private val TAG = "DatabaseApplication"
    // You can add application-wide initialization or state management here

    companion object {
        private lateinit var instance: DatabaseApplication
        lateinit var container: AppContainer
        fun getInstance(): DatabaseApplication {
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
        val mapKitKey = ApiKeyFactory.getApiKey(this)
        Log.d(TAG, "MapKit key detected: len=${mapKitKey.length}, startsWith=${mapKitKey.take(6)}")
        if (mapKitKey.isNotBlank() && mapKitKey != "YOUR_YANDEX_MAPKIT_API_KEY") {
            MapKitFactory.setApiKey(mapKitKey)
            MapKitFactory.initialize(this)
        }
        instance = this
        container = AppDataContainer(this)
        // Perform any initialization here
    }
}