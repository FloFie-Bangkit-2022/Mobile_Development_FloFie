package com.capstone.flofie.ui.search

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.flofie.model.SearchHistory
import com.capstone.flofie.repository.SearchHistoryRepository

class SearchViewModel(application: Application) : ViewModel() {
    private val searchHistoryRepository : SearchHistoryRepository = SearchHistoryRepository(application)

    fun insert(history: SearchHistory) {
        searchHistoryRepository.insert(history)
    }

    fun getAllSerachHistory() : LiveData<List<SearchHistory>> {
        return searchHistoryRepository.getAllSearchHistory()
    }

    fun deleteAllSearchHistory() = searchHistoryRepository.deleteAllSearcgHistory()
}