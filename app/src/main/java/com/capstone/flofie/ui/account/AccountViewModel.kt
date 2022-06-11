package com.capstone.flofie.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class AccountViewModel(private val preferences: LoginPreferences, private val uuid : String?) : ViewModel() {

    fun saveLoginStatus(isLogin : Boolean) {
        viewModelScope.launch {
            preferences.saveLoginStatus(isLogin)
        }
    }

    val _userProfile = MutableLiveData<String>()
    val userProfile : LiveData<String> = _userProfile
}