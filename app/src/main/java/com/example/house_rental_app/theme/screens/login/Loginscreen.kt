package com.example.house_rental_app.theme.screens.login

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.house_rental_app.R
import com.example.house_rental_app.data.SharedViewModel
import com.example.house_rental_app.data.UserViewModel
import com.example.house_rental_app.navigation.ROUTE_ALL_LISTINGS
import com.example.house_rental_app.navigation.ROUTE_REGISTER
import com.example.house_rental_app.utils.ValidationUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Loginscreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val userViewModel: UserViewModel = viewModel()
    val currentUser by userViewModel.currentUser.observeAsState()
    val error by userViewModel.error.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Состояния для ошибок валидации
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        Log.d("LoginScreen", "currentUser changed: $currentUser")
        if (currentUser != null) {
            isLoading = false
            sharedViewModel.setUserId(currentUser!!)
            Log.d("LoginScreen", "Navigating to home with userId: ${currentUser}")
            navController.navigate(ROUTE_ALL_LISTINGS) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(error) {
        Log.d("LoginScreen", "error changed: $error")
        if (error == true) {
            isLoading = false
            generalError = "Неверный email или пароль"
            // Сбрасываем ошибку через 3 секунды
            delay(3000)
            generalError = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Общая ошибка
        if (generalError != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = generalError!!,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Поле email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
                generalError = null
            },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = emailError != null,
            supportingText = {
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле пароля
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                generalError = null
            },
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = passwordError != null,
            supportingText = {
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка входа
        Button(
            onClick = {
                Log.d("LoginScreen", "Login button clicked with email: $email")

                // Валидация
                val validationResult = ValidationUtils.validateLogin(email, password)

                if (!validationResult.isValid) {
                    when {
                        email.isEmpty() || !ValidationUtils.isValidEmail(email) -> emailError = validationResult.errorMessage
                        password.isEmpty() -> passwordError = validationResult.errorMessage
                    }
                    return@Button
                }

                isLoading = true
                userViewModel.loginUser(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(stringResource(R.string.login_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                navController.navigate(ROUTE_REGISTER)
            },
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.no_account))
        }
    }
}