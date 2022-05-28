package com.capstone.flofie.ui.account.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ProfileSettingsViewModel : ViewModel() {

    val myCalendar: Calendar = Calendar.getInstance()

    var _date = MutableLiveData<String>()
    val date : LiveData<String> = _date
}