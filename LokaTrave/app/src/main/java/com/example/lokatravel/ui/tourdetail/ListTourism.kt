package com.example.lokatravel.ui.tourdetail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lokatravel.R

@Suppress("DEPRECATION")
class ListTourism : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TourismListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_tourism)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TourismListAdapter(emptyList())
        recyclerView.adapter = adapter

        // Get data from intent
        val tourTitle = intent.getStringExtra("TOUR_TITLE")
        supportActionBar?.title = tourTitle

        // Replace with your logic to fetch data and update adapter
        val dataList = fetchDataForTourismList()
        adapter.updateData(dataList)

        // Set item click listener
        adapter.setOnItemClickListener(object : TourismListAdapter.OnItemClickListener {
            override fun onItemClick(item: TourismItem) {
                // Handle item click here
                val intent = Intent(this@ListTourism, TourDetailActivity::class.java).apply {
                    putExtra("TOUR_TITLE", item.nama) // Pass data to TourDetailActivity
                    putExtra("TOUR_IMAGE", item.imageResId)
                }
                startActivity(intent)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchDataForTourismList(): List<TourismItem> {
        // Replace this with your logic to fetch data for the RecyclerView
        // For example, you can fetch from a database or network
        return listOf(
            TourismItem("Place 1", "Category 1", "Description 1", R.drawable.contoh),
            TourismItem("Place 2", "Category 2", "Description 2", R.drawable.contoh),
            TourismItem("Place 3", "Category 3", "Description 3", R.drawable.contoh)
            // Add more items as needed
        )
    }
}
