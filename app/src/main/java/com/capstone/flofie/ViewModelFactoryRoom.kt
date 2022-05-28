package com.capstone.flofie

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.flofie.ui.search.SearchViewModel
import java.lang.IllegalArgumentException

class ViewModelFactoryRoom private constructor(private val mApplication : Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE : ViewModelFactoryRoom? = null

        @JvmStatic
        fun getInstance(application: Application) : ViewModelFactoryRoom {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactoryRoom(application)
                }
            }
            return INSTANCE as ViewModelFactoryRoom
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(mApplication!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}