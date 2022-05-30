package com.capstone.flofie.ui.main.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import kotlinx.coroutines.launch

class SigninViewModel(private val preferences: LoginPreferences) : ViewModel() {

    fun saveLoginStatus(isLogin : Boolean) {
        viewModelScope.launch {
            preferences.saveLoginStatus(isLogin)
        }
    }
}