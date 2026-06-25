package com.example.house_rental_app

import android.content.Context
import android.content.pm.PackageManager

object ApiKeyFactory {
    private const val MAPKIT_API_KEY = "com.yandex.mapkit.API_KEY"

    fun getApiKey(context: Context): String {
        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        val manifestKey = appInfo.metaData?.getString(MAPKIT_API_KEY).orEmpty()
        return manifestKey.ifBlank { BuildConfig.YANDEX_MAPKIT_API_KEY }
    }
}
