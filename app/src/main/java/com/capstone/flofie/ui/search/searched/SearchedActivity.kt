package com.capstone.flofie.ui.search.searched

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.flofie.adapter.HomeFlowerAdapater
import com.capstone.flofie.databinding.ActivitySearchedBinding
import com.capstone.flofie.model.Flower
import android.view.MotionEvent

import com.capstone.flofie.ui.detail.DetailActivity


class SearchedActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchedBinding

    companion object {
        const val EXTRA_FLOWER = "flower"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setFlowerItems()
        setExtraFlower()
        setupSearchTouch()

        val refreshLayout = binding.homeSwipeRefreshLayout
        refreshLayout.setOnRefreshListener {
            // this is just for template
            Handler().postDelayed({
                refreshLayout.isRefreshing = false
            }, 2000)
        }

    }

    private fun setFlowerItems() {
        val listFlowers = ArrayList<Flower>()
        listFlowers.add(Flower("Blue Tulip", "https://cdn.pixabay.com/photo/2013/07/21/13/00/rose-165819__340.jpg"))
        listFlowers.add(Flower("Rose", "https://i.pinimg.com/originals/53/13/f7/5313f7882176c8716d9ad03f9cc1a370.jpg"))
        listFlowers.add(Flower("Sunflower", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/A_sunflower.jpg/360px-A_sunflower.jpg"))
        listFlowers.add(Flower("Tulip", "https://us.123rf.com/450wm/stockerhero/stockerhero1905/stockerhero190500232/122163433-beautiful-various-tulips-at-field-in-holland.jpg?ver=6"))
        listFlowers.add(Flower("Lotus", "https://klikhijau.com/wp-content/uploads/2020/10/bunga-teratai.jpg"))

        val homeFlowerAdapter = HomeFlowerAdapater(listFlowers)
        binding.homeRecyclerItem.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.homeRecyclerItem.adapter = homeFlowerAdapter

        homeFlowerAdapter.setOnItemClickCallback(object : HomeFlowerAdapater.OnItemClick{
            override fun onItemClickedCallback() {
                startActivity(Intent(this@SearchedActivity, DetailActivity::class.java))
            }
        })
    }

    private fun setExtraFlower() {
        val searchedFlower = intent.getStringExtra(EXTRA_FLOWER)
        binding.searchedFlower.inputText.setText(searchedFlower)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearchTouch() {
        binding.searchedFlower.inputText.setOnTouchListener( { v, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                onBackPressed()
            }
            true
        })

        binding.searchedFlower.searchBack.setOnClickListener {
            onBackPressed()
        }
    }
}