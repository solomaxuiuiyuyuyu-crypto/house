package com.example.house_rental_app.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            UserViewModel()
        }
        initializer {
            HouseViewModel()
        }
    }
}