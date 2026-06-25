package com.example.house_rental_app.data

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.house_rental_app.DatabaseApplication
import com.example.house_rental_app.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val appContext = DatabaseApplication.getInstance()
    private val tag = "SettingsViewModel"

    private val _isDarkTheme = MutableStateFlow(UserPreferencesManager.isDarkTheme(appContext))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _language = MutableStateFlow(UserPreferencesManager.getLanguage(appContext))
    val language: StateFlow<String> = _language.asStateFlow()

    fun setDarkTheme(isDark: Boolean) {
        Log.d(tag, "setDarkTheme($isDark)")
        _isDarkTheme.value = isDark
        UserPreferencesManager.setDarkTheme(appContext, isDark)
    }

    fun setLanguage(language: String) {
        Log.d(tag, "setLanguage($language) current=${_language.value}")
        if (_language.value == language) return
        _language.value = language
        UserPreferencesManager.setLanguage(appContext, language)
        Log.d(tag, "Applying locales=$language")
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }
}
