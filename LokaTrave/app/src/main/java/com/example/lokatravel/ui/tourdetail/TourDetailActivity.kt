package com.example.lokatravel.ui.tourdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lokatravel.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class TourDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var ratingBar: RatingBar
    private lateinit var feedbackEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tour_detail)

        // Apply window insets to adjust for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle back button click
        val backButton: ImageButton = findViewById(R.id.arrow_back)
        backButton.setOnClickListener {
            finish() // End the activity and return to the previous one
        }

        // Load the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize views
        ratingBar = findViewById(R.id.tour_rating)
        feedbackEditText = findViewById(R.id.tour_feedback)

        // Setup rating bar listener
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Example: Update a TextView with the selected rating
            feedbackEditText.setText("You gave this tour a $rating star rating.")
        }

        // Setup submit button click listener
        val submitButton: Button = findViewById(R.id.button_submit_rating)
        submitButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val feedback = feedbackEditText.text.toString()
            saveRatingAndFeedback(rating, feedback)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set default map type
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Enable map controls
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        // Add a marker and move camera to a location
        val location = LatLng(4.492986, 97.963628) // Replace with your actual coordinates
        mMap.addMarker(MarkerOptions().position(location).title("Marker in Langsa"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    private fun saveRatingAndFeedback(rating: Int, feedback: String) {
        // Implement your logic to save the rating and feedback
        // For example, you can send them to a server, save to local database, etc.
        // Here we just print them for demonstration
        println("Rating: $rating, Feedback: $feedback")
    }
}