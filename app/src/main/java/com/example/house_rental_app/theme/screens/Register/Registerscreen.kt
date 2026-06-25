package com.example.house_rental_app.theme.screens.Register

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
import com.example.house_rental_app.R
import com.example.house_rental_app.data.UserViewModel
import com.example.house_rental_app.entity.UserEntity
import com.example.house_rental_app.navigation.ROUTE_LOGIN
import com.example.house_rental_app.utils.ValidationUtils
import kotlinx.coroutines.delay  // ← ДОБАВИТЬ ЭТУ СТРОКУ

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    val userViewModel: UserViewModel = viewModel()
    val error by userViewModel.error.observeAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Состояния для ошибок валидации
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var registrationSuccess by remember { mutableStateOf(false) }

    // Обработка успешной регистрации
    LaunchedEffect(error) {
        Log.d("RegisterScreen", "error changed: $error, isLoading: $isLoading")

        if (error == false && isLoading) {
            // Успешная регистрация
            Log.d("RegisterScreen", "Registration successful!")
            registrationSuccess = true
            isLoading = false

            // Ждём 2 секунды и переходим на экран входа
            delay(2000)
            Log.d("RegisterScreen", "Navigating to login screen")
            navController.navigate(ROUTE_LOGIN) {
                popUpTo(ROUTE_LOGIN) { inclusive = true }
                launchSingleTop = true
            }
        } else if (error == true && isLoading) {
            // Ошибка регистрации
            Log.d("RegisterScreen", "Registration failed!")
            isLoading = false
            generalError = "Ошибка регистрации. Возможно, пользователь с таким email уже существует"
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
            text = stringResource(R.string.register_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Сообщение об успешной регистрации
        if (registrationSuccess) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Регистрация успешна!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Перенаправление на вход...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

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

        // Поле имени пользователя
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = null
                generalError = null
            },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = usernameError != null,
            supportingText = {
                if (usernameError != null) {
                    Text(
                        text = usernameError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            enabled = !isLoading && !registrationSuccess
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            enabled = !isLoading && !registrationSuccess
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле пароля
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
                confirmPasswordError = null
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
                } else {
                    Text(
                        text = "Минимум 6 символов, хотя бы одна буква и одна цифра",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            enabled = !isLoading && !registrationSuccess
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле подтверждения пароля
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = null
                generalError = null
            },
            label = { Text("Подтвердите пароль") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = confirmPasswordError != null,
            supportingText = {
                if (confirmPasswordError != null) {
                    Text(
                        text = confirmPasswordError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            enabled = !isLoading && !registrationSuccess
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле телефона
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                phoneError = null
                generalError = null
            },
            label = { Text(stringResource(R.string.phone)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = phoneError != null,
            supportingText = {
                if (phoneError != null) {
                    Text(
                        text = phoneError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            enabled = !isLoading && !registrationSuccess
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка регистрации
        Button(
            onClick = {
                Log.d("RegisterScreen", "Register button clicked")

                // Комплексная валидация
                val validationResult = ValidationUtils.validateRegistration(
                    username = username,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    phoneNumber = phone
                )

                if (!validationResult.isValid) {
                    generalError = validationResult.errorMessage
                    if (ValidationUtils.isValidUsername(username).errorMessage != null) {
                        usernameError = ValidationUtils.isValidUsername(username).errorMessage
                    }
                    if (!ValidationUtils.isValidEmail(email)) {
                        emailError = "Введите корректный email адрес"
                    }
                    if (ValidationUtils.isValidPassword(password).errorMessage != null) {
                        passwordError = ValidationUtils.isValidPassword(password).errorMessage
                    }
                    if (ValidationUtils.doPasswordsMatch(password, confirmPassword).errorMessage != null) {
                        confirmPasswordError = ValidationUtils.doPasswordsMatch(password, confirmPassword).errorMessage
                    }
                    if (ValidationUtils.isValidPhoneNumber(phone).errorMessage != null) {
                        phoneError = ValidationUtils.isValidPhoneNumber(phone).errorMessage
                    }
                    return@Button
                }

                isLoading = true
                val user = UserEntity(
                    username = username,
                    email = email,
                    password = password,
                    phoneNumber = phone
                )
                userViewModel.registerUser(user)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && !registrationSuccess
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(stringResource(R.string.register_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                navController.navigate(ROUTE_LOGIN)
            },
            enabled = !isLoading
        ) {
            Text(stringResource(R.string.have_account))
        }
    }
}