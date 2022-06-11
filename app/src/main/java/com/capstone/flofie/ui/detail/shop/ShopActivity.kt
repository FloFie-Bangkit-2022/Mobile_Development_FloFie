package com.capstone.flofie.ui.detail.shop

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.flofie.R
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.adapter.ShopItemsAdapater
import com.capstone.flofie.databinding.ActivityShopBinding
import com.capstone.flofie.model.Shop
import com.capstone.flofie.model.ShopItems
import com.capstone.flofie.ui.main.MainActivity
import com.capstone.flofie.ui.map.ShopMapsActivity

class ShopActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShopBinding
    private lateinit var shopViewModel : ShopViewModel

    companion object {
        const val EXTRA_SHOP_DATA = "shop data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupButton()
        setupShopItems()
    }

    private fun setupViewModel() {
        shopViewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[ShopViewModel::class.java]
    }

    private fun setupShopItems() {
        val imageList = listOf<Int>(R.drawable.flower, R.drawable.bucket_flower, R.drawable.sprout, R.drawable.pot_flower)
        val nameList = listOf<String>("Bunga", "Bucket", "Benih", "Dengan Pot")

        val listShopItems = ArrayList<ShopItems>()

        for (x in 0..imageList.size-1) {
            val newItem = ShopItems(imageList[x], nameList[x], false)
            listShopItems.add(newItem)
        }

        val adapter = ShopItemsAdapater(listShopItems)
        binding.shopItemsRecyclerView.adapter = adapter
        binding.shopItemsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.shopItemsRecyclerView.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object : ShopItemsAdapater.OnItemClick {
            override fun onItemClickedCallback(position: Int) {
                if (listShopItems[position].isSelected == true) {
                    listShopItems[position].isSelected = false
                    adapter.notifyDataSetChanged()
                }
                else {
                    listShopItems[position].isSelected = true
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setupButton() {

        val shop = intent.getParcelableExtra<Shop>(EXTRA_SHOP_DATA)
        binding.shopActionMap.setOnClickListener {

            val gmIntent = Uri.parse("geo:0,0?q=${shop?.lat},${shop?.long}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmIntent)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(this.packageManager!!) != null) {
                startActivity(mapIntent)
            } else {
                val shopMapIntent = Intent(this, ShopMapsActivity::class.java)
                shopMapIntent.putExtra(ShopMapsActivity.EXTRA_SHOP, shop)
                startActivity(shopMapIntent)
            }
        }
    }
}