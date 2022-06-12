package com.capstone.flofie.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.flofie.adapter.HomeFlowerAdapater
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.databinding.FragmentHomeBinding
import com.capstone.flofie.model.Flower
import com.capstone.flofie.ui.search.SearchActivity

import android.os.Handler
import com.capstone.flofie.ui.detail.DetailActivity


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setViewModel()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    private fun setViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[HomeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeFakeSearchBar.mainSearch.setOnClickListener {
            val optionCompact : ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    Pair(binding.homeFakeSearchBar.mainSearch, "search_flower_transaction")
                )
            val searchIntent = Intent(activity, SearchActivity::class.java)
            startActivity(searchIntent, optionCompact.toBundle())
        }

        setFlowerItems()

        val refreshLayout = binding.homeSwipeRefreshLayout
        refreshLayout.setOnRefreshListener {
            // this is just for template
            Handler().postDelayed({
                refreshLayout.isRefreshing = false
            }, 2000)
        }
    }

    private fun setFlowerItems() {
        val listFlowers = ArrayList<Flower>()
        listFlowers.add(Flower("Dandelion", "https://ichef.bbci.co.uk/news/976/cpsprodpb/8398/production/_103888633_hi016427699.jpg"))
        listFlowers.add(Flower("Rose", "https://i.pinimg.com/originals/53/13/f7/5313f7882176c8716d9ad03f9cc1a370.jpg"))
        listFlowers.add(Flower("Sunflower", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/A_sunflower.jpg/360px-A_sunflower.jpg"))
        listFlowers.add(Flower("Tulip", "https://us.123rf.com/450wm/stockerhero/stockerhero1905/stockerhero190500232/122163433-beautiful-various-tulips-at-field-in-holland.jpg?ver=6"))
        listFlowers.add(Flower("Lotus", "https://klikhijau.com/wp-content/uploads/2020/10/bunga-teratai.jpg"))

        val homeFlowerAdapter = HomeFlowerAdapater(listFlowers)
        homeFlowerAdapter.setOnItemClickCallback(object : HomeFlowerAdapater.OnItemClick {
            override fun onItemClickedCallback() {
                startActivity(Intent(activity, DetailActivity::class.java))
            }
        })
        binding.homeRecyclerItem.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.homeRecyclerItem.adapter = homeFlowerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}