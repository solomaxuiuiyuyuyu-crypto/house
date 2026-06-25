package com.example.house_rental_app.utils

import android.util.Patterns
import java.util.regex.Pattern

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Пароль не может быть пустым")
            password.length < 6 -> ValidationResult(false, "Пароль должен содержать минимум 6 символов")
            password.length > 30 -> ValidationResult(false, "Пароль не должен превышать 30 символов")
            !password.any { it.isDigit() } -> ValidationResult(false, "Пароль должен содержать хотя бы одну цифру")
            !password.any { it.isLetter() } -> ValidationResult(false, "Пароль должен содержать хотя бы одну букву")
            else -> ValidationResult(true, null)
        }
    }
    fun isValidUsername(username: String): ValidationResult {
        return when {
            username.isEmpty() -> ValidationResult(false, "Имя пользователя не может быть пустым")
            username.length < 3 -> ValidationResult(false, "Имя пользователя должно содержать минимум 3 символа")
            username.length > 20 -> ValidationResult(false, "Имя пользователя не должно превышать 20 символов")
            !username.matches(Regex("^[a-zA-Z0-9_]+$")) -> ValidationResult(false, "Имя пользователя может содержать только буквы, цифры и подчёркивание")
            else -> ValidationResult(true, null)
        }
    }
    fun isValidPhoneNumber(phone: String): ValidationResult {
        return when {
            phone.isEmpty() -> ValidationResult(true, null) // Телефон может быть пустым
            phone.length < 10 -> ValidationResult(false, "Номер телефона слишком короткий")
            phone.length > 15 -> ValidationResult(false, "Номер телефона слишком длинный")
            !phone.matches(Regex("^[0-9+\\-() ]+$")) -> ValidationResult(false, "Номер телефона содержит недопустимые символы")
            else -> ValidationResult(true, null)
        }
    }

    // Проверка совпадения паролей (для регистрации)
    fun doPasswordsMatch(password: String, confirmPassword: String): ValidationResult {
        return if (password == confirmPassword) {
            ValidationResult(true, null)
        } else {
            ValidationResult(false, "Пароли не совпадают")
        }
    }

    // Комплексная валидация регистрации
    fun validateRegistration(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        phoneNumber: String
    ): RegistrationValidationResult {
        val usernameResult = isValidUsername(username)
        if (!usernameResult.isValid) {
            return RegistrationValidationResult(false, usernameResult.errorMessage)
        }

        if (!isValidEmail(email)) {
            return RegistrationValidationResult(false, "Введите корректный email адрес")
        }

        val passwordResult = isValidPassword(password)
        if (!passwordResult.isValid) {
            return RegistrationValidationResult(false, passwordResult.errorMessage)
        }

        val passwordsMatchResult = doPasswordsMatch(password, confirmPassword)
        if (!passwordsMatchResult.isValid) {
            return RegistrationValidationResult(false, passwordsMatchResult.errorMessage)
        }

        val phoneResult = isValidPhoneNumber(phoneNumber)
        if (!phoneResult.isValid) {
            return RegistrationValidationResult(false, phoneResult.errorMessage)
        }

        return RegistrationValidationResult(true, null)
    }

    // Комплексная валидация логина
    fun validateLogin(email: String, password: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult(false, "Введите email")
            !isValidEmail(email) -> ValidationResult(false, "Введите корректный email адрес")
            password.isEmpty() -> ValidationResult(false, "Введите пароль")
            else -> ValidationResult(true, null)
        }
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

data class RegistrationValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)