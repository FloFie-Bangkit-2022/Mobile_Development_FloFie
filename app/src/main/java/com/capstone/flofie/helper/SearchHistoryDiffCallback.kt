package com.capstone.flofie.helper

import androidx.recyclerview.widget.DiffUtil
import com.capstone.flofie.model.SearchHistory

class SearchHistoryDiffCallback (private val mOldSearchHistory : List<SearchHistory>, private val mNewSearchHistory: List<SearchHistory>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldSearchHistory.size
    }

    override fun getNewListSize(): Int {
        return mNewSearchHistory.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldSearchHistory[oldItemPosition].content == mNewSearchHistory[newItemPosition].content
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newSearchHistory = mNewSearchHistory[newItemPosition]
        val oldSearchHistory = mOldSearchHistory[oldItemPosition]
        return oldSearchHistory.content == newSearchHistory.content
    }

}