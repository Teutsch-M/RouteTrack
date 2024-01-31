package com.example.routetrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.routetrack.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TrackingFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tracking, container, false)

        view?.apply {
            initializeView(this)
        }
        mapView.onCreate(savedInstanceState)

        return view
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private fun initializeView(view: View){
        mapView = view.findViewById(R.id.mapView)
        mapView.getMapAsync {
            map = it
        }
    }

}