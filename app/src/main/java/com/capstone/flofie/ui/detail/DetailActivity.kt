package com.capstone.flofie.ui.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.viewpager2.widget.ViewPager2
import com.capstone.flofie.R
import com.capstone.flofie.adapter.SectionPageAdapter
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import com.capstone.flofie.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    companion object{
        private val TAB_TITLES = intArrayOf(R.string.tab_layout_description, R.string.tab_layout_shop)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setTabLayout()
    }

    private fun setTabLayout() {
        val sectionPageAdapter = SectionPageAdapter(this@DetailActivity)
        val viewPager : ViewPager2 = binding.detailViewPager

        viewPager.adapter = sectionPageAdapter

        val tabs : TabLayout = binding.detailTabLayout
        TabLayoutMediator(tabs, viewPager) {
            tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}