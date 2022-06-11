package com.capstone.flofie.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.flofie.ui.detail.DescriptionFragment
import com.capstone.flofie.ui.detail.ShopListFragment

class SectionPageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when (position) {
            0 -> fragment = DescriptionFragment()
            1 -> fragment = ShopListFragment()
            }
        return fragment as Fragment
    }
}