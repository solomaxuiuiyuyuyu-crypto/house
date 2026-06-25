package com.example.house_rental_app

import android.content.Context

object SessionManager {
    private const val PREFS_NAME = "house_rental_prefs"
    private const val KEY_USER_ID = "logged_in_user_id"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUserId(context: Context, userId: String) {
        prefs(context).edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): String? {
        return prefs(context).getString(KEY_USER_ID, null)
    }

    fun clearSession(context: Context) {
        prefs(context).edit().remove(KEY_USER_ID).apply()
    }
}
