package com.capstone.flofie.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.flofie.R
import com.capstone.flofie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        window.setBackgroundDrawableResource(R.drawable.aaron_burden_flower_unsplash)
    }
}