package com.example.house_rental_app.theme.screens.menuscreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.house_rental_app.R
import com.example.house_rental_app.data.SharedViewModel
import com.example.house_rental_app.navigation.ROUTE_ADD_PROPERTY
import com.example.house_rental_app.navigation.ROUTE_ALL_LISTINGS
import com.example.house_rental_app.navigation.ROUTE_HOME
import com.example.house_rental_app.navigation.ROUTE_MAP
import com.example.house_rental_app.navigation.ROUTE_MY_LISTINGS
import com.example.house_rental_app.navigation.ROUTE_SETTINGS
import com.example.house_rental_app.navigation.ROUTE_USER_PROFILE

@Composable
fun MenuBar(navController: NavController, currentRoute: String, sharedViewModel: SharedViewModel) {
    // Цвета иконок в зависимости от темы
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val icons = listOf(
            Pair(ROUTE_ALL_LISTINGS, Icons.Default.Home) to stringResource(R.string.menu_all_listings),
            Pair(ROUTE_MY_LISTINGS, Icons.Default.LocationOn) to stringResource(R.string.menu_my_listings),
            Pair(ROUTE_ADD_PROPERTY, Icons.Default.Add) to stringResource(R.string.menu_add_property),
            Pair(ROUTE_MAP, Icons.Default.Map) to stringResource(R.string.menu_map),
            Pair(ROUTE_USER_PROFILE, Icons.Default.Person) to stringResource(R.string.menu_profile),
            Pair(ROUTE_SETTINGS, Icons.Default.Settings) to stringResource(R.string.menu_settings),
            Pair(ROUTE_HOME, Icons.Default.ExitToApp) to stringResource(R.string.menu_logout)
        )

        icons.forEach { (routeIcon, contentDescription) ->
            val (route, icon) = routeIcon
            IconButton(
                onClick = {
                    if (route == ROUTE_HOME) {
                        sharedViewModel.logout()
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(route)
                    }
                }
            ) {
                Icon(
                    icon,
                    contentDescription = contentDescription,
                    tint = if (currentRoute == route) activeColor else inactiveColor,
                    modifier = Modifier.size(28.dp)
                )
                Box(
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .size(width = 28.dp, height = 3.dp)
                        .background(if (currentRoute == route) activeColor else Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun getCurrentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}