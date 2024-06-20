package com.example.lokatravel

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.example.lokatravel.databinding.ActivityMainBinding
import com.example.lokatravel.ui.home.HomeFragment
import com.example.lokatravel.ui.news.NewsFragment
import com.example.lokatravel.ui.profile.ProfileFragment

@Suppress("DEPRECATION", "SameParameterValue")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFragmentId: Int = R.id.beranda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.beranda -> {
                    currentFragmentId = R.id.beranda
                    replaceFragment(HomeFragment())
                    animateBottomNavigationIcon(menuItem, true)
                    true
                }
                R.id.news -> {
                    currentFragmentId = R.id.news
                    replaceFragment(NewsFragment())
                    animateBottomNavigationIcon(menuItem, true)
                    true
                }
                R.id.profile -> {
                    currentFragmentId = R.id.profile
                    replaceFragment(ProfileFragment())
                    animateBottomNavigationIcon(menuItem, true)
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt("currentFragmentId", R.id.beranda)
        }

        when (currentFragmentId) {
            R.id.beranda -> replaceFragment(HomeFragment())
            R.id.profile -> replaceFragment(ProfileFragment())
            R.id.news -> replaceFragment(NewsFragment())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentFragmentId", currentFragmentId)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun animateBottomNavigationIcon(menuItem: MenuItem, isSelected: Boolean) {
        val icon = menuItem.icon
        icon ?: return

        val scaleUp = if (isSelected) 1.2f else 1.0f
        val scaleDown = if (isSelected) 1.0f else 1.2f

        val scaleX = ObjectAnimator.ofFloat(icon, "scaleX", scaleUp)
        scaleX.duration = 300
        scaleX.interpolator = AccelerateDecelerateInterpolator()

        val scaleY = ObjectAnimator.ofFloat(icon, "scaleY", scaleUp)
        scaleY.duration = 300
        scaleY.interpolator = AccelerateDecelerateInterpolator()

        scaleX.start()
        scaleY.start()

        // Reset scale after animation completes
        scaleX.doOnEnd {
            val scaleXReverse = ObjectAnimator.ofFloat(icon, "scaleX", scaleDown)
            scaleXReverse.duration = 0
            scaleXReverse.start()

            val scaleYReverse = ObjectAnimator.ofFloat(icon, "scaleY", scaleDown)
            scaleYReverse.duration = 0
            scaleYReverse.start()
        }
    }
}
