package com.capstone.flofie.database.searchHistory

import androidx.lifecycle.LiveData
import androidx.room.*
import com.capstone.flofie.model.SearchHistory

@Dao
interface SearchHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: SearchHistory)

    @Query("DELETE FROM searchhistory")
    fun deleteAllSearchHistory()

    @Query("SELECT * FROM searchhistory")
    fun getAllSearchHistory() : LiveData<List<SearchHistory>>
}