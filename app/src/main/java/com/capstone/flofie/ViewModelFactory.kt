package com.capstone.flofie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import com.capstone.flofie.ui.account.AccountViewModel
import com.capstone.flofie.ui.account.profile.ProfileSettingsViewModel
import com.capstone.flofie.ui.camera.CameraViewModel
import com.capstone.flofie.ui.detail.DetailViewModel
import com.capstone.flofie.ui.detail.shop.ShopViewModel
import com.capstone.flofie.ui.home.HomeViewModel
import com.capstone.flofie.ui.main.MainViewModel
import com.capstone.flofie.ui.main.signin.SigninViewModel
import com.capstone.flofie.ui.main.signup.SignupViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val preferences: LoginPreferences? = null, private val uuid : String? = null) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel() as T
            }
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel() as T
            }
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(preferences!!, uuid) as T
            }
            modelClass.isAssignableFrom(ProfileSettingsViewModel::class.java) -> {
                ProfileSettingsViewModel() as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel() as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(preferences!!) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel() as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(preferences!!) as T
            }
            modelClass.isAssignableFrom(ShopViewModel::class.java) -> {
                ShopViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}