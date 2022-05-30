package com.capstone.flofie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.flofie.databinding.SearchHistoryItemBinding
import com.capstone.flofie.model.SearchHistory
import com.capstone.flofie.helper.SearchHistoryDiffCallback

class SearchHistoryAdapter : RecyclerView.Adapter<SearchHistoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback

    private val listSearchHistory = ArrayList<SearchHistory>()
    fun setListSearchHistory(listHistory : List<SearchHistory>) {
        val diffCallback = SearchHistoryDiffCallback(listSearchHistory, listHistory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listSearchHistory.clear()
        listSearchHistory.addAll(listHistory)
        diffResult.dispatchUpdatesTo(this)
    }

    class ListViewHolder(private var binding : SearchHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: SearchHistory) {
            binding.searchHistoryText.text = history.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = SearchHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSearchHistory[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSearchHistory[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listSearchHistory.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(data : SearchHistory)
    }
}