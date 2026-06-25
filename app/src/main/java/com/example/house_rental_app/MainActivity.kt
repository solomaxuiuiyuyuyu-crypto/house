package com.example.house_rental_app

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.house_rental_app.data.SharedViewModel
import com.example.house_rental_app.data.SettingsViewModel
import com.example.house_rental_app.navigation.AppNavHost
import com.example.house_rental_app.navigation.ROUTE_ALL_LISTINGS
import com.example.house_rental_app.navigation.ROUTE_HOME
import com.example.house_rental_app.theme.HouseRentAppTheme
import com.example.house_rental_app.theme.screens.menuscreens.MenuBar
import com.example.house_rental_app.theme.screens.menuscreens.getCurrentRoute  // ← ВАЖНЫЙ ИМПОРТ

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = UserPreferencesManager.getLanguage(this)
        val targetLocales = LocaleListCompat.forLanguageTags(language)
        if (AppCompatDelegate.getApplicationLocales() != targetLocales) {
            AppCompatDelegate.setApplicationLocales(targetLocales)
        }
        setContent {
            MainAppStructure()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppStructure() {
    val navController = rememberNavController()
    val currentRoute = getCurrentRoute(navController) ?: ""
    val sharedViewModel: SharedViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val userId = sharedViewModel.userId.observeAsState()
    val startDestination = if (userId.value.isNullOrBlank()) ROUTE_HOME else ROUTE_ALL_LISTINGS

    Log.println(Log.INFO, "In Main", userId.toString())
    Log.println(Log.INFO, "Current Route", currentRoute)

    HouseRentAppTheme(
        darkTheme = isDarkTheme,
        dynamicColor = false
    ) {
        Scaffold(
            topBar = {
                // Показываем MenuBar только на экранах, где нужна навигация
                if (currentRoute != "home" && currentRoute != "login" && currentRoute != "register") {
                    MenuBar(
                        navController = navController,
                        currentRoute = currentRoute,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        ) { paddingValues ->
            AppNavHost(
                navController = navController,
                startDestination = startDestination,
                paddingValues = paddingValues,
                sharedViewModel = sharedViewModel,
                settingsViewModel = settingsViewModel
            )
        }
    }
}