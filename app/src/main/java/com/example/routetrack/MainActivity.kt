package com.example.routetrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.routetrack.utility.Constants.ACTION_SHOW_TRACKING
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var navigationView: BottomNavigationView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var appBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navToTrackingFrag(intent)

        navigationView = findViewById(R.id.nav_view)
        floatingButton = findViewById(R.id.addRouteButton)
        appBar = findViewById(R.id.bottomAppBar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.routeFragment, R.id.refuelFragment, R.id.summaryFragment, R.id.settingsFragment -> {
                    navigationView.visibility = View.VISIBLE
                    floatingButton.visibility = View.VISIBLE
                    appBar.visibility = View.VISIBLE
                }
                else -> {
                    navigationView.visibility = View.GONE
                    floatingButton.visibility = View.GONE
                    appBar.visibility = View.GONE
                }
            }
        }

        floatingButton.setOnClickListener {
            navHostFragment.findNavController().navigateUp()
            navHostFragment.findNavController().navigate(R.id.trackingFragment)
        }

    }

    private fun navToTrackingFrag(intent: Intent?){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        if (intent?.action == ACTION_SHOW_TRACKING)
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navToTrackingFrag(intent)
    }

}