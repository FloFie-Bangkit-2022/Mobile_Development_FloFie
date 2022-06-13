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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "login")

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mFirebaseAuth = FirebaseAuth.getInstance()

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
        mainViewModel.getLoginStatus().observe(this, { loginStatus ->

            val firebaseUser : FirebaseUser? = mFirebaseAuth.currentUser

            if (firebaseUser != null) {
                val mFirebaseUserID = FirebaseAuth.getInstance().currentUser?.uid
//                Log.d("CEK_UUID", mFirebaseUserID!!)

                if (loginStatus == true) {
                    logedIn(mFirebaseUserID)
                    finish()
                }
                else if (loginStatus == false) {
                    mainViewModel.saveLoginStatus(true)
                    logedIn(mFirebaseUserID)

                    finish()
                }
            }
        })
    }

    private fun logedIn(userID : String?) {
        val intetStart = Intent(this, MainHostActivity::class.java)
        intetStart.putExtra(MainHostActivity.EXTRA_USER_ID, userID)
        startActivity(intetStart)
    }
}