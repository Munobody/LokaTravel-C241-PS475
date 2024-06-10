package com.example.lokatravel.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import androidx.recyclerview.widget.RecyclerView
import com.example.lokatravel.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewLeft = view.findViewById<RecyclerView>(R.id.recyclerViewLeft)
        val recyclerViewRight = view.findViewById<RecyclerView>(R.id.recyclerViewRight)

        recyclerViewLeft.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        recyclerViewRight.layoutManager = LinearLayoutManager(context, VERTICAL, false)

        val leftDataList = fetchLeftData()
        val rightDataList = fetchRightData()

        val leftAdapter = HomeAdapter(leftDataList)
        val rightAdapter = HomeAdapter(rightDataList)

        recyclerViewLeft.adapter = leftAdapter
        recyclerViewRight.adapter = rightAdapter
    }

    private fun fetchLeftData(): List<HomeViewModel> {
        val dataList = mutableListOf<HomeViewModel>()
        dataList.add(HomeViewModel("Jakarta", R.drawable.contoh))
        dataList.add(HomeViewModel("Surabaya", R.drawable.contoh))
        dataList.add(HomeViewModel("Medan", R.drawable.contoh))
        dataList.add(HomeViewModel("Medan", R.drawable.contoh))
        return dataList
    }

    private fun fetchRightData(): List<HomeViewModel> {
        val dataList = mutableListOf<HomeViewModel>()
        dataList.add(HomeViewModel("Bandung", R.drawable.contoh))
        dataList.add(HomeViewModel("Bandung", R.drawable.contoh))
        dataList.add(HomeViewModel("Bandung", R.drawable.contoh))
        dataList.add(HomeViewModel("Bandung", R.drawable.contoh))
        return dataList
    }
}
