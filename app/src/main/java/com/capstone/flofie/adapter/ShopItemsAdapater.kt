package com.capstone.flofie.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.flofie.databinding.FlowerItemBinding
import com.capstone.flofie.databinding.ShopMoreItemBinding
import com.capstone.flofie.model.ShopItems

class ShopItemsAdapater(private val listItems : ArrayList<ShopItems>) : RecyclerView.Adapter<ShopItemsAdapater.ListViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClick

    class ListViewHolder(var binding: ShopMoreItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ShopMoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.shopMoreItemImage.setImageResource(listItems[position].image)
        holder.binding.shopMoreItemName.setText(listItems[position].name)
        holder.binding.shopMoreItemCard.setCardBackgroundColor(if (listItems[position].isSelected) Color.GREEN else Color.WHITE)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClickedCallback(position)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setOnItemClickCallback(onItemClick: OnItemClick) {
        this.onItemClickCallback = onItemClick
    }

    interface OnItemClick {
        fun onItemClickedCallback(position : Int)
    }
}