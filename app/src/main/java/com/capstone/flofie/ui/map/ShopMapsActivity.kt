package com.capstone.flofie.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.flofie.R
import com.capstone.flofie.databinding.ActivityShopMapsBinding
import com.capstone.flofie.model.Shop
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ShopMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShopMapsBinding

    companion object {
        const val EXTRA_SHOP = "shop"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShopMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val location = intent.getParcelableExtra<Shop>(EXTRA_SHOP)
        val shopLocation = LatLng(location?.lat!!, location?.long!!)
        mMap.addMarker(MarkerOptions().position(shopLocation).title("${location.name}"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(shopLocation))
    }
}