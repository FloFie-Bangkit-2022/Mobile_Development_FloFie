package com.capstone.flofie.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.flofie.adapter.ShopAdapter
import com.capstone.flofie.databinding.FragmentShopBinding
import com.capstone.flofie.model.Shop
import com.capstone.flofie.ui.map.ShopMapsActivity

class ShopFragment : Fragment() {

    private var _binding : FragmentShopBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createShopItems()
    }

    private fun createShopItems() {
        val listShops = ArrayList<Shop>()
        for (x in 1..12) {
            val newShop = Shop(x,
                "Fancy Florist Tembalang",
                "4.5",
                "Jl. Sumurboto III No.9, Sumurboto, Kec. Banyumanik, Kota Semarang, Jawa Tengah 50269",
                "https://lunaflorist.com/wp-content/uploads/2018/09/rangkaian-bunga-meja-semarang-murah-300ribu.jpg",
                -7.053611988976657,
                110.4276809997613)
            listShops.add(newShop)
        }

        val shopItemAdapter = ShopAdapter(listShops)

        shopItemAdapter.setOnItemClicekCallback(object : ShopAdapter.ItemClickCallback{
            override fun onItemClickedCallback(shop: Shop) {

                val gmIntent = Uri.parse("geo:0,0?q=${shop?.lat},${shop?.long}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmIntent)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(activity?.packageManager!!) != null) {
                    startActivity(mapIntent)
                } else {
                    val shopMapIntent = Intent(activity, ShopMapsActivity::class.java)
                    shopMapIntent.putExtra(ShopMapsActivity.EXTRA_SHOP, shop)
                    Log.d("CEK_SHOP", shop.toString())
                    startActivity(shopMapIntent)
                }
            }
        })

        binding.detailShopRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.detailShopRecyclerView.adapter = shopItemAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}