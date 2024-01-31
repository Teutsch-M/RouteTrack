package com.example.routetrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
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

        navigationView = findViewById(R.id.nav_view)
        floatingButton = findViewById(R.id.addRouteButton)
        appBar = findViewById(R.id.bottomAppBar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().addOnDestinationChangedListener{_,destination,_ ->
            when(destination.id){
                R.id.routeFragment, R.id.summaryFragment -> {
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


}