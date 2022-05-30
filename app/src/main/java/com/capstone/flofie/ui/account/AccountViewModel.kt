package com.capstone.flofie.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import kotlinx.coroutines.launch

class AccountViewModel(private val preferences: LoginPreferences) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Account Fragment"
    }

    val text : LiveData<String> = _text

    fun saveLoginStatus(isLogin : Boolean) {
        viewModelScope.launch {
            preferences.saveLoginStatus(isLogin)
        }
    }
}