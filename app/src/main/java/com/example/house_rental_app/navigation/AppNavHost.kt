package com.example.house_rental_app.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.house_rental_app.data.SharedViewModel
import com.example.house_rental_app.data.SettingsViewModel
import com.example.house_rental_app.theme.screens.Register.RegisterScreen
import com.example.house_rental_app.theme.screens.home.HomeScreen
import com.example.house_rental_app.theme.screens.login.Loginscreen
import com.example.house_rental_app.theme.screens.menuscreens.AddProperty
import com.example.house_rental_app.theme.screens.menuscreens.AllListings
import com.example.house_rental_app.theme.screens.menuscreens.MapScreen
import com.example.house_rental_app.theme.screens.menuscreens.MyListings
import com.example.house_rental_app.theme.screens.menuscreens.UserProfile
import com.example.house_rental_app.theme.screens.property.DetailedProperty
import com.example.house_rental_app.theme.screens.property.ContactLandlord
import com.example.house_rental_app.theme.screens.settings.SettingsScreen

// AppNavHost.kt - проверь startDestination
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_HOME,  // Всегда HOME как стартовый
    paddingValues: PaddingValues,
    sharedViewModel: SharedViewModel,
    settingsViewModel: SettingsViewModel
) {
    val userId = sharedViewModel.userId.observeAsState()
    val isLoggedIn = !userId.value.isNullOrBlank()

    // Отслеживаем изменения userId и перенаправляем
    LaunchedEffect(userId.value) {
        if (isLoggedIn) {
            // Если пользователь залогинен, идём на ALL_LISTINGS
            if (navController.currentDestination?.route != ROUTE_ALL_LISTINGS) {
                navController.navigate(ROUTE_ALL_LISTINGS) {
                    popUpTo(ROUTE_HOME) { inclusive = true }
                    launchSingleTop = true
                }
            }
        } else {
            // Если не залогинен, идём на HOME
            if (navController.currentDestination?.route != ROUTE_HOME &&
                navController.currentDestination?.route != ROUTE_LOGIN &&
                navController.currentDestination?.route != ROUTE_REGISTER) {
                navController.navigate(ROUTE_HOME) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable(ROUTE_LOGIN) {
            Loginscreen(navController, sharedViewModel)
        }

        composable(ROUTE_REGISTER) {
            RegisterScreen(navController)
        }

        composable(ROUTE_HOME) {
            HomeScreen(navController)
        }

        composable(ROUTE_ALL_LISTINGS) {
            Box(modifier = Modifier.padding(paddingValues)) {
                AllListings(navController, sharedViewModel)
            }
        }

        composable(ROUTE_MY_LISTINGS) {
            Box(modifier = Modifier.padding(paddingValues)) {
                MyListings(navController, sharedViewModel)
            }
        }

        composable(ROUTE_USER_PROFILE) {
            Box(modifier = Modifier.padding(paddingValues)) {
                UserProfile(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }

        composable("$ROUTE_DETAILED_PROPERTY/{houseKey}") { backStackEntry ->
            val houseKey = backStackEntry.arguments?.getString("houseKey") ?: return@composable
            Box(modifier = Modifier.padding(paddingValues)) {
                DetailedProperty(
                    navController = navController,
                    houseKey = houseKey
                )
            }
        }

        composable(ROUTE_ADD_PROPERTY) {
            Box(modifier = Modifier.padding(paddingValues)) {
                AddProperty(navController, sharedViewModel)
            }
        }

        composable(ROUTE_MAP) {
            Box(modifier = Modifier.padding(paddingValues)) {
                MapScreen()
            }
        }

        composable(ROUTE_SETTINGS) {
            Box(modifier = Modifier.padding(paddingValues)) {
                SettingsScreen(settingsViewModel)
            }
        }

        composable("$ROUTE_CONTACT_LANDLORD/{ownerKey}") { backStackEntry ->
            val ownerKey = backStackEntry.arguments?.getString("ownerKey") ?: return@composable
            Box(modifier = Modifier.padding(paddingValues)) {
                ContactLandlord(
                    navController = navController,
                    ownerKey = ownerKey
                )
            }
        }
    }
}