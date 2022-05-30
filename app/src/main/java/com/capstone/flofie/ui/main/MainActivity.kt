package com.capstone.flofie.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.flofie.MainHostActivity
import com.capstone.flofie.R
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import com.capstone.flofie.databinding.ActivityMainBinding

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "login")

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setViewModel()

        checkLogin()

        window.setBackgroundDrawableResource(R.drawable.aaron_burden_flower_unsplash)
    }

    private fun setViewModel() {

        val preference = LoginPreferences.getInstance(dataStore)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(preference)
        ) [MainViewModel::class.java]
    }

    fun getDataStore() : DataStore<Preferences> {
        return dataStore
    }

    fun checkLogin() {
        mainViewModel.getLoginStatus().observe(this, {
            if (it == true) {
                startActivity(Intent(this, MainHostActivity::class.java))
                finish()
            }
        })
    }
}