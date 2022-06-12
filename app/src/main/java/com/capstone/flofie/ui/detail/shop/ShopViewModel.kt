package com.capstone.flofie.ui.detail.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopViewModel : ViewModel() {

    val _total = MutableLiveData<Int>().apply {
        value = 0
    }
    val total : LiveData<Int> = _total

}