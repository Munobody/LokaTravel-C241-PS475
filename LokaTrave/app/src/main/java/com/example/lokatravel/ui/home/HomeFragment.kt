package com.example.lokatravel.ui.home
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lokatravel.R
import com.example.lokatravel.ui.tourdetail.ListTourism
import com.example.lokatravel.ui.tourdetail.TourDetailActivity

class HomeFragment : Fragment() {

    private lateinit var handlerLeft: Handler
    private lateinit var handlerRight: Handler
    private lateinit var runnableLeft: Runnable
    private lateinit var runnableRight: Runnable

    private lateinit var recyclerViewLeft: RecyclerView
    private lateinit var recyclerViewRight: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerViewLeft = view.findViewById(R.id.recyclerViewLeft)
        recyclerViewRight = view.findViewById(R.id.recyclerViewRight)
        searchEditText = view.findViewById(R.id.searchEditText)
        searchButton = view.findViewById(R.id.searchButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()

        // Start auto-scroll handlers
        startAutoScrollHandlers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handlerLeft.removeCallbacks(runnableLeft)
        handlerRight.removeCallbacks(runnableRight)
    }

    private fun setupRecyclerViews() {
        recyclerViewLeft.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewRight.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val leftDataList = fetchLeftData()
        val rightDataList = fetchRightData()

        val leftAdapter = HomeAdapter(leftDataList,
            object : HomeAdapter.OnLeftItemClickListener {
                override fun onLeftItemClick(data: HomeViewModel) {
                    navigateToTourDetail(data.name)
                }
            },
            null // No right click listener for left adapter
        )

        val rightAdapter = HomeAdapter(rightDataList,
            null, // No left click listener for right adapter
            object : HomeAdapter.OnRightItemClickListener {
                override fun onRightItemClick(data: HomeViewModel) {
                    navigateToListTourism(data.name)
                }
            }
        )

        recyclerViewLeft.adapter = leftAdapter
        recyclerViewRight.adapter = rightAdapter
    }

    private fun startAutoScrollHandlers() {
        handlerLeft = Handler(Looper.getMainLooper())
        handlerRight = Handler(Looper.getMainLooper())

        runnableLeft = object : Runnable {
            var position = 0
            override fun run() {
                val leftDataList = fetchLeftData()
                if (leftDataList.isNotEmpty()) {
                    recyclerViewLeft.smoothScrollToPosition(position)
                    position = (position + 1) % leftDataList.size
                }
                handlerLeft.postDelayed(this, 3000) // Scroll every 3 seconds
            }
        }

        runnableRight = object : Runnable {
            var position = 0
            override fun run() {
                val rightDataList = fetchRightData()
                if (rightDataList.isNotEmpty()) {
                    recyclerViewRight.smoothScrollToPosition(position)
                    position = (position + 1) % rightDataList.size
                }
                handlerRight.postDelayed(this, 3000) // Scroll every 3 seconds
            }
        }

        handlerLeft.post(runnableLeft)
        handlerRight.post(runnableRight)
    }

    private fun fetchLeftData(): List<HomeViewModel> {
        val dataList = mutableListOf<HomeViewModel>()
        dataList.add(HomeViewModel("Jakarta", R.drawable.contoh))
        dataList.add(HomeViewModel("Yogyakarta", R.drawable.contoh))
        dataList.add(HomeViewModel("Bandung", R.drawable.contoh))
        dataList.add(HomeViewModel("Semarang", R.drawable.contoh))
        return dataList
    }

    private fun fetchRightData(): List<HomeViewModel> {
        val dataList = mutableListOf<HomeViewModel>()
        dataList.add(HomeViewModel("Surabaya", R.drawable.surabaya))
        dataList.add(HomeViewModel("Bali", R.drawable.bali))
        dataList.add(HomeViewModel("Medan", R.drawable.medan))
        dataList.add(HomeViewModel("Mojokerto", R.drawable.mojokerto))
        dataList.add(HomeViewModel("Jakarta", R.drawable.jakarta))
        dataList.add(HomeViewModel("Yogyakarta", R.drawable.jogja))
        dataList.add(HomeViewModel("Bandung", R.drawable.bandung))
        dataList.add(HomeViewModel("Semarang", R.drawable.semarang))
        return dataList
    }

    private fun navigateToTourDetail(cityName: String) {
        val intent = Intent(requireActivity(), TourDetailActivity::class.java).apply {
            putExtra("CITY_NAME", cityName)
        }
        startActivity(intent)
    }

    private fun navigateToListTourism(tourTitle: String) {
        val intent = Intent(requireActivity(), ListTourism::class.java).apply {
            putExtra("TOUR_TITLE", tourTitle)
        }
        startActivity(intent)
    }
}
