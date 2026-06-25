package com.example.house_rental_app

import android.content.Context

object UserPreferencesManager {
    private const val PREFS_NAME = "house_rental_user_prefs"
    private const val KEY_IS_DARK_THEME = "is_dark_theme"
    private const val KEY_LANGUAGE = "language"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isDarkTheme(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_IS_DARK_THEME, false)
    }

    fun setDarkTheme(context: Context, isDark: Boolean) {
        prefs(context).edit().putBoolean(KEY_IS_DARK_THEME, isDark).apply()
    }

    fun getLanguage(context: Context): String {
        return prefs(context).getString(KEY_LANGUAGE, "ru").orEmpty().ifBlank { "ru" }
    }

    fun setLanguage(context: Context, language: String) {
        prefs(context).edit().putString(KEY_LANGUAGE, language).apply()
    }
}
