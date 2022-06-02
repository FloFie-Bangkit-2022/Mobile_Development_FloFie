package com.capstone.flofie.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: LoginPreferences) : ViewModel() {

    fun getLoginStatus() : LiveData<Boolean> {
        return preferences.getLoginStatus().asLiveData()
    }

    fun saveLoginStatus(isLogin : Boolean) {
        viewModelScope.launch {
            preferences.saveLoginStatus(isLogin)
        }
    }
}