package com.example.house_rental_app.data

import android.util.Log
import com.example.house_rental_app.DatabaseApplication
import com.example.house_rental_app.SessionManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> = _userId

    init {
        _userId.value = SessionManager.getUserId(DatabaseApplication.getInstance())
    }

    fun setUserId(id: String?) {
        _userId.value = id
        if (id.isNullOrBlank()) {
            SessionManager.clearSession(DatabaseApplication.getInstance())
        } else {
            SessionManager.saveUserId(DatabaseApplication.getInstance(), id)
        }
        Log.println(Log.INFO, "Setting userid", _userId.value.toString())
    }

    fun logout() {
        setUserId(null)
    }
}