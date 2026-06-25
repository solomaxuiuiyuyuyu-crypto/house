package com.example.house_rental_app.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.house_rental_app.DatabaseApplication
import com.example.house_rental_app.entity.UserEntity
import kotlinx.coroutines.launch

data class UserDetails(
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

data class UserUiState(
    val userDetails: UserDetails = UserDetails(),
    val isEntryValid: Boolean = false
)

class UserViewModel : ViewModel() {
    private val TAG = "UserViewModel"

    private val _currentUser = MutableLiveData<String?>()
    val currentUser: LiveData<String?> = _currentUser

    private val _userDetails = MutableLiveData<UserEntity?>()
    val userDetails: LiveData<UserEntity?> = _userDetails

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val userRepository = DatabaseApplication.container.userRepository

    var userUiState by mutableStateOf(UserUiState())
        private set

    fun updateUserUiState(userDetails: UserDetails) {
        userUiState = UserUiState(
            userDetails = userDetails,
            isEntryValid = userDetails.email.isNotBlank() && userDetails.password.isNotBlank()
        )
    }

    fun setUser(user: String?) {
        _currentUser.value = user
    }

    fun setError(error: Boolean) {
        _error.value = error
    }

    fun registerUser(user: UserEntity) {
        viewModelScope.launch {
            try {
                val success = userRepository.registerUser(user)
                if (success) {
                    Log.d(TAG, "User registered successfully")
                    _error.postValue(false)  // ← Используйте postValue вместо value
                } else {
                    _error.postValue(true)
                    Log.d(TAG, "User registration failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error registering user: ${e.message}", e)
                _error.postValue(true)
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val userId = userRepository.loginUser(email, password)
                if (userId != null) {
                    _currentUser.postValue(userId)
                    _error.value = false
                    Log.d(TAG, "User logged in: $userId")
                } else {
                    _error.value = true
                    Log.d(TAG, "Login failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error logging in: ${e.message}", e)
                _error.value = true
            }
        }
    }
    // Добавьте в UserViewModel.kt:

    fun changePassword(userId: String, oldPassword: String, newPassword: String): Boolean {
        var result = false
        viewModelScope.launch {
            try {
                // Сначала получаем пользователя
                val user = userRepository.getUserById(userId)
                if (user != null && user.verifyPassword(oldPassword)) {
                    // Создаём копию с новым паролем
                    val updatedUser = user.copy()
                    user.hashAndSetPassword(newPassword)  // ← НОВЫЙ КОД
                    result = userRepository.updateUser(userId, updatedUser)
                    if (result) {
                        Log.d(TAG, "Password changed successfully")
                    } else {
                        Log.d(TAG, "Password change failed")
                    }
                } else {
                    Log.d(TAG, "Old password is incorrect")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error changing password: ${e.message}", e)
            }
        }
        return result
    }

    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                _userDetails.postValue(user)
                Log.d(TAG, "User fetched: $user")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user: ${e.message}", e)
                _userDetails.postValue(null)
            }
        }
    }

    fun updateUser(userId: String, user: UserEntity) {
        viewModelScope.launch {
            try {
                val success = userRepository.updateUser(userId, user)
                if (success) {
                    fetchUserById(userId)
                    Log.d(TAG, "User updated successfully")
                } else {
                    Log.d(TAG, "User update failed")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user: ${e.message}", e)
            }
        }
    }
}