package com.example.routetrack.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import com.example.routetrack.R
import com.example.routetrack.services.TrackingService
import com.example.routetrack.utility.Constants.ACTION_PAUSE_SERVICE
import com.example.routetrack.utility.Constants.ACTION_START_RESUME_SERVICE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TrackingFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var buttonTimer: Button
    private lateinit var buttonFinish: Button
    private var isTracking = false
    private var coordinates = mutableListOf<MutableList<LatLng>>()

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
            registerListeners(this)
            observeData()
        }

        mapView.getMapAsync {
            map = it
            addAllLine()
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
        buttonTimer = view.findViewById(R.id.buttonTimer)
        buttonFinish = view.findViewById(R.id.button_finishRoute)
    }

    private fun registerListeners(view: View){
        buttonTimer.setOnClickListener {
            commandService(if (isTracking)
                ACTION_PAUSE_SERVICE
            else
                ACTION_START_RESUME_SERVICE)
        }
    }

    private fun commandService(action: String) = Intent(requireContext(), TrackingService::class.java).also {
        it.action = action
        requireContext().startService(it)
    }

    private fun addLine(){
        if (coordinates.isNotEmpty() && coordinates.last().size > 1){
            val lastCoord = coordinates.last().last()
            val bLastCoord = coordinates.last()[coordinates.last().size - 2]
            val lineOptions = PolylineOptions()
                .color(Color.RED)
                .width(12F)
                .add(bLastCoord)
                .add(lastCoord)
            map.addPolyline(lineOptions)
        }
    }

    private fun addAllLine(){
        for (line in coordinates){
            val lineOptions = PolylineOptions()
                .color(Color.RED)
                .width(12F)
                .addAll(line)
            map.addPolyline(lineOptions)
        }
    }

    private fun toggleCamera(){
        if (coordinates.isNotEmpty() && coordinates.last().isNotEmpty())
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates.last().last(), 16F))
    }

    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if (!isTracking){
            buttonTimer.text = "Start"
            buttonFinish.visibility = View.VISIBLE
        }
        else{
            buttonTimer.text = "STOP"
            buttonFinish.visibility = View.GONE
        }
    }

    private fun observeData(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.coordinates.observe(viewLifecycleOwner, Observer {
            coordinates = it
            addLine()
            toggleCamera()
        })
    }

}