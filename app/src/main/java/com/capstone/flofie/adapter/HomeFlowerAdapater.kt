package com.capstone.flofie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.flofie.R
import com.capstone.flofie.databinding.FlowerItemBinding
import com.capstone.flofie.model.Flower

class HomeFlowerAdapater(private val listFlowers : ArrayList<Flower>) : RecyclerView.Adapter<HomeFlowerAdapater.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClick

    class ListViewHolder(var binding: FlowerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = FlowerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(listFlowers[position].image).placeholder(R.drawable.placeholder).into(holder.binding.homeImageFlower)
        holder.binding.homeTextNameFlower.text = listFlowers[position].name
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClickedCallback()
        }
    }

    override fun getItemCount(): Int {
        return listFlowers.size
    }

    fun setOnItemClickCallback(onItemClick: OnItemClick) {
        this.onItemClickCallback = onItemClick
    }

    interface OnItemClick{
        fun onItemClickedCallback()
    }
}