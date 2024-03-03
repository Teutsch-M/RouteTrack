package com.example.routetrack.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.routetrack.R
import com.example.routetrack.database.Converter
import com.example.routetrack.database.Route
import com.example.routetrack.services.TrackingService
import com.example.routetrack.ui.viewmodels.RouteViewModel
import com.example.routetrack.utility.Constants.ACTION_PAUSE_SERVICE
import com.example.routetrack.utility.Constants.ACTION_START_RESUME_SERVICE
import com.example.routetrack.utility.Constants.ACTION_STOP_SERVICE
import com.example.routetrack.utility.TrackingUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import kotlin.math.round


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TrackingFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "TrackingFragment"
    private val viewModel: RouteViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var buttonTimer: Button
    private lateinit var buttonFinish: Button
    private lateinit var timerText: TextView
    private lateinit var vehicleText: TextView
    private lateinit var spinner: Spinner
    private lateinit var sp: SharedPreferences
    private var map: GoogleMap? = null
    private var marker: Marker? = null
    private var markerOptions: MarkerOptions? = null
    private var myPos: LatLng = LatLng(0.0, 0.0)
    private lateinit var myIcon: BitmapDescriptor
    private var vehicle: Int = 0
    private var isTracking = false
    private var coordinates = mutableListOf<MutableList<LatLng>>()
    private var routeTime: Long = 0
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

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
        }



        mapView.onCreate(savedInstanceState)

        observeData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isTracking && routeTime.toInt() == 0) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            vehicle = 0
                            marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
                        }
                        1 -> {
                            vehicle = 1
                            marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike))
                        }
                        2 -> {
                            vehicle = 2
                            marker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_walk))
                        }
                    }
                    Log.d(TAG, vehicle.toString())
                    sp.edit().putInt("vehicle", vehicle).apply()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }



        mapView.getMapAsync {
            map = it
            addAllLine()
            db.collection("routes").get()
                .addOnSuccessListener { documents ->
                    for (doc in documents){
                        if (doc.id != userId) {
                            val geoPoint = doc.getGeoPoint("userLocation")
                            if (geoPoint != null) {
                                val latitude = geoPoint.latitude
                                val longitude = geoPoint.longitude
                                val latLng = LatLng(latitude, longitude)
                                it.addMarker(MarkerOptions().position(latLng).title(doc.id))
                            }
                        }
                    }
                }
                .addOnFailureListener {  ex ->
                    Log.e(TAG, ex.message, ex)
                }

            setMarkerOptions()
        }


    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        addAllLine()
        observeData()
        vehicle = sp.getInt("vehicle", 0)
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
        timerText = view.findViewById(R.id.timer)
        vehicleText = view.findViewById(R.id.vehicleText)
        spinner = view.findViewById(R.id.vehicleOption)
        sp = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    private fun registerListeners(view: View){
        buttonTimer.setOnClickListener {
            vehicleText.visibility = View.GONE
            spinner.visibility = View.GONE
            commandService(if (isTracking)
                ACTION_PAUSE_SERVICE
            else
                ACTION_START_RESUME_SERVICE)
        }

        buttonFinish.setOnClickListener {
            seeWholeRoute()
            saveRoute()
        }
    }

    private fun setMarkerOptions() {

        db.collection("routes").document(userId).get()
            .addOnSuccessListener {
                when (vehicle) {
                    0 -> myIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_car)
                    1 -> myIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_bike)
                    2 -> myIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_walk)
                }
                val geoPoint = it.getGeoPoint("userLocation")
                if (geoPoint != null) {
                    val lat = geoPoint.latitude
                    val long = geoPoint.longitude
                    myPos = LatLng(lat, long)
                }

                markerOptions = MarkerOptions()
                    .icon(myIcon)
                    .position(myPos)
                    .title("My Position")

                marker = map?.addMarker(markerOptions!!)
            }
            .addOnFailureListener {  ex ->
                Log.e(TAG, ex.message, ex)
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
            map?.addPolyline(lineOptions)
            marker?.position = LatLng(lastCoord.latitude, lastCoord.longitude)
        }
    }

    private fun addAllLine(){
        for (line in coordinates){
            val lineOptions = PolylineOptions()
                .color(Color.RED)
                .width(12F)
                .addAll(line)
            map?.addPolyline(lineOptions)
        }
    }

    private fun toggleCamera(){
        if (coordinates.isNotEmpty() && coordinates.last().isNotEmpty())
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates.last().last(), 16F))
    }

    private fun seeWholeRoute(){
        val bounds = LatLngBounds.Builder()
        for (line in coordinates)
            for (coord in line)
                bounds.include(coord)

        map?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), mapView.width, mapView.height, (mapView.height * 0.15).toInt()))
    }

    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if (!isTracking && routeTime > 0){
            buttonTimer.text = "Start"
            buttonFinish.visibility = View.VISIBLE
            vehicleText.visibility = View.GONE
            spinner.visibility = View.GONE
        }
        else if (isTracking){
            buttonTimer.text = "STOP"
            buttonFinish.visibility = View.GONE
            vehicleText.visibility = View.GONE
            spinner.visibility = View.GONE
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

        TrackingService.stopperTime.observe(viewLifecycleOwner, Observer {
            routeTime = it
            val timeInText = TrackingUtility.formatTime(routeTime, true)
            timerText.text = timeInText
        })
    }


    private fun saveRoute(){
        map?.snapshot {
            val img = Converter.fromBitmap(it)
            var distance = 0F
            for (line in coordinates)
                distance += TrackingUtility.getDistance(line)
            val avgSpeed = round((distance / (routeTime / 1000f / 60 / 60)) * 10) / 10f
            val date = Calendar.getInstance().timeInMillis
            val route = Route(
                vehicle,
                img,
                distance,
                routeTime,
                avgSpeed,
                date
            )
            viewModel.addRoute(route)
            Toast.makeText(activity, "Route successfully added!", Toast.LENGTH_LONG).show()
            finishRoute()
        }
    }

    private fun finishRoute(){
        commandService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_routeFragment)
    }

}