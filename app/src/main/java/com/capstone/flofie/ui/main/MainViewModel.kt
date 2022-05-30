package com.capstone.flofie.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.flofie.database.loginPreferences.LoginPreferences

class MainViewModel(private val preferences: LoginPreferences) : ViewModel() {

    fun getLoginStatus() : LiveData<Boolean> {
        return preferences.getLoginStatus().asLiveData()
    }
}