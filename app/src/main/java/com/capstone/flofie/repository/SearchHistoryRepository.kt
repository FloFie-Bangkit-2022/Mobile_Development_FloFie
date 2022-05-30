package com.capstone.flofie.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.capstone.flofie.database.searchHistory.SearchHistoryDAO
import com.capstone.flofie.database.searchHistory.SearchHistoryRoomDatabase
import com.capstone.flofie.model.SearchHistory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SearchHistoryRepository(application: Application) {
    private val searchHistoryDao : SearchHistoryDAO
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = SearchHistoryRoomDatabase.getDatabase(application)
        searchHistoryDao = db.searchHistoryDao()
    }

    fun getAllSearchHistory() : LiveData<List<SearchHistory>> = searchHistoryDao.getAllSearchHistory()

    fun insert(history: SearchHistory) {
        executorService.execute {searchHistoryDao.insert(history)}
    }

    fun deleteAllSearcgHistory() = searchHistoryDao.deleteAllSearchHistory()
}