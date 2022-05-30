package com.capstone.flofie.database.searchHistory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.flofie.model.SearchHistory

@Database(entities = [SearchHistory::class], version = 1)
abstract class SearchHistoryRoomDatabase : RoomDatabase() {
    abstract fun searchHistoryDao() : SearchHistoryDAO

    companion object {
        @Volatile
        private var INSTANCE : SearchHistoryRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : SearchHistoryRoomDatabase {
            if (INSTANCE == null) {
                synchronized(SearchHistoryRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SearchHistoryRoomDatabase::class.java, "search_history_database").allowMainThreadQueries().build()
                }
            }
            return INSTANCE as SearchHistoryRoomDatabase
        }
    }
}