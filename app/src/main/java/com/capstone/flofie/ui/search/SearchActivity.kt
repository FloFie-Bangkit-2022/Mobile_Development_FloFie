package com.capstone.flofie.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.flofie.R
import com.capstone.flofie.ViewModelFactoryRoom
import com.capstone.flofie.adapter.SearchHistoryAdapter
import com.capstone.flofie.databinding.ActivitySearchBinding
import com.capstone.flofie.model.SearchHistory
import com.capstone.flofie.ui.search.searched.SearchedActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        searchHistoryAdapter = SearchHistoryAdapter()

        binding.recyclerSearchHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearchHistory.setHasFixedSize(true)
        binding.recyclerSearchHistory.adapter = searchHistoryAdapter

        searchHistoryAdapter.setOnItemClickCallback(object : SearchHistoryAdapter.OnItemClickCallback{
            override fun onItemClicked(data: SearchHistory) {
                binding.searchFlower.inputText.setText(data.content)
            }
        })

        searchViewModel = setupViewModel(this@SearchActivity)

        setFocus()
        setButton()

        binding.searchFlower.inputText.setOnEditorActionListener { textView, i, keyEvent ->
            return@setOnEditorActionListener when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (binding.searchFlower.inputText.text?.isNotEmpty() == true) {
                        val searchText = binding.searchFlower.inputText.text.toString()
                        saveSearchHistory(searchText)

                        moveWithAnimation(searchText)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewModel(activity: AppCompatActivity) : SearchViewModel {
        val factoryInstance = ViewModelFactoryRoom.getInstance(this.application)
        return ViewModelProvider(activity, factoryInstance).get(SearchViewModel::class.java)
    }

    private fun moveWithAnimation(extraFlower : String) {
        val optionCompact : ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@SearchActivity,
                Pair(binding.searchFlower.inputLayout, "searched_flower_transaction")
            )
        val searchedIntent = Intent(this, SearchedActivity::class.java)
        searchedIntent.putExtra(SearchedActivity.EXTRA_FLOWER, extraFlower)
        startActivity(searchedIntent, optionCompact.toBundle())
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.getAllSerachHistory().observe(this, {
            searchHistoryAdapter.setListSearchHistory(it)
        })
    }

    private fun saveSearchHistory(text: String) {
        val history = SearchHistory()
        history?.content = text
        searchViewModel.insert(history)
    }

    private fun setFocus() {
        val searchInput = binding.searchFlower?.inputText
        searchInput.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setButton() {
        binding?.searchFlower?.searchBack.setOnClickListener {
            onBackPressed()
        }

        binding.searchClearHistory.setOnClickListener {
            val deleteHistoryDialogBuilder = AlertDialog.Builder(this, R.style.LogoutDialogBackground)
            deleteHistoryDialogBuilder.setMessage("Hapus Semua History?")
            deleteHistoryDialogBuilder.setCancelable(true)

            deleteHistoryDialogBuilder.setPositiveButton("Yes") { dialog, id ->
                    searchViewModel.deleteAllSearchHistory()
                    searchHistoryAdapter.notifyDataSetChanged()
            }
            deleteHistoryDialogBuilder.setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }

            val alertDeleteDialog = deleteHistoryDialogBuilder.create()
            alertDeleteDialog.show()
        }
    }
}