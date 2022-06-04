package com.capstone.flofie.ui.main.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SignupViewModel : ViewModel() {

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    val _isActive = MutableLiveData<Boolean>()
    val isActive : LiveData<Boolean> = _isActive
}