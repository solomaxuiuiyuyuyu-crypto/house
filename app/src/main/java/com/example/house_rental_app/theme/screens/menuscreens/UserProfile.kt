// UserProfile.kt - исправленная версия
package com.example.house_rental_app.theme.screens.menuscreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.house_rental_app.R
import com.example.house_rental_app.data.SharedViewModel
import com.example.house_rental_app.data.UserViewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfile(navController: NavController, sharedViewModel: SharedViewModel) {
    val userViewModel: UserViewModel = viewModel()
    val userId = sharedViewModel.userId.value
    val userDetails = userViewModel.userDetails.value

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showOnlyEmail by remember { mutableStateOf(true) }
    var showOnlyPhone by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }

    // Загружаем данные пользователя
    LaunchedEffect(userId) {
        if (userId != null) {
            userViewModel.fetchUserById(userId)
        }
    }

    // Обновляем поля формы когда загрузятся данные
    LaunchedEffect(userDetails) {
        userDetails?.let { user ->
            username = user.username
            email = user.email
            phoneNumber = user.phoneNumber
            showOnlyEmail = user.showOnlyEmail
            showOnlyPhone = user.showOnlyPhone
        }
    }

    // Убираем Scaffold, используем просто Column
    // MenuBar будет отображаться в MainActivity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isEditing) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.username)) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email)) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(stringResource(R.string.phone)) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    userDetails?.let { user ->
                        if (userId != null) {
                            val updatedUser = user.copy(
                                username = username,
                                email = email,
                                phoneNumber = phoneNumber,
                                showOnlyEmail = showOnlyEmail,
                                showOnlyPhone = showOnlyPhone
                            )
                            userViewModel.updateUser(userId, updatedUser)
                            isEditing = false
                        }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.profile_save))
            }
        } else {
            Text(stringResource(R.string.username_with_value, username))
            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.email_with_value, userDetails?.email ?: stringResource(R.string.not_available)))
            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.phone_with_value, userDetails?.phoneNumber ?: stringResource(R.string.not_available)))
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { isEditing = true }) {
                Text(stringResource(R.string.profile_edit))
            }
        }
    }
}