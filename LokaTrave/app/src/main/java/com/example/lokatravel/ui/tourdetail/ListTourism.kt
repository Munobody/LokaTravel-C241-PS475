package com.example.lokatravel.ui.tourdetail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lokatravel.R
import com.example.lokatravel.data.retrofit.ApiConfig
import com.example.lokatravel.data.response.JakartaResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

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

        // Fetch data from API
        if (tourTitle != null) {
            fetchTourismData(tourTitle.lowercase()) // Ensure the title is in lowercase
        }
    }

    private fun fetchTourismData(city: String) {
        val formattedCity = city.replaceFirstChar { it.uppercase() } // Capitalize the first letter
        val apiService = ApiConfig.getThirdApiService().getTourismDataByCity(formattedCity)
        apiService.enqueue(object : Callback<List<JakartaResponseItem>> {
            override fun onResponse(call: Call<List<JakartaResponseItem>>, response: Response<List<JakartaResponseItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Log the entire response for debugging
                        Log.d("ListTourism", "Response: $it")

                        adapter.updateData(it.map { item ->
                            TourismItem(item.placeName ?: "Unknown", item.category ?: "Unknown", "No details", R.drawable.contoh)
                        })
                    }
                } else {
                    Log.e("ListTourism", "Failed to fetch data: ${response.message()}")
                    Toast.makeText(this@ListTourism, "Failed to fetch data: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<JakartaResponseItem>>, t: Throwable) {
                Log.e("ListTourism", "Error: ${t.message}")
                Toast.makeText(this@ListTourism, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
