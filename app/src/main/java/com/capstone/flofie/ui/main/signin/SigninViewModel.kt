package com.capstone.flofie.ui.main.signin

import androidx.lifecycle.*
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import kotlinx.coroutines.launch

class SigninViewModel(private val preferences: LoginPreferences) : ViewModel() {

    fun saveLoginStatus(isLogin : Boolean) {
        viewModelScope.launch {
            preferences.saveLoginStatus(isLogin)
        }
    }

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    val _isActive = MutableLiveData<Boolean>()
    val isActive : LiveData<Boolean> = _isActive
}