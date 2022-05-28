package com.capstone.flofie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.flofie.R
import com.capstone.flofie.databinding.ShopItemBinding
import com.capstone.flofie.model.Shop

class ShopAdapter(private val listShop : ArrayList<Shop>) : RecyclerView.Adapter<ShopAdapter.ListViewHolder>() {

    class ListViewHolder(var binding : ShopItemBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: ItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ShopItemBinding.inflate(LayoutInflater.from(parent.context,), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(R.drawable.placeholder).into(holder.binding.shopCardImage)
        holder.apply {
            binding.apply {
                shopCardName.text = listShop[position].name
                shopCardRatting.text = listShop[position].rating
                shopCardAdress.text = listShop[position].address
            }
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClickedCallback(listShop[position]) }
    }

    override fun getItemCount(): Int {
        return listShop.size
    }

    fun setOnItemClicekCallback(onItemClickCallback: ItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface ItemClickCallback{
        fun onItemClickedCallback(shop : Shop)
    }
}