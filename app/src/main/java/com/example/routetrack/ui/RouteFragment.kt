package com.example.routetrack.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routetrack.R
import com.example.routetrack.adapters.RouteAdapter
import com.example.routetrack.ui.viewmodels.RouteViewModel
import com.example.routetrack.utility.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.routetrack.utility.TrackingUtility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RouteFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "RouteFragment"
    private val viewModel: RouteViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RouteAdapter
    private lateinit var spinner: Spinner
    private lateinit var vehicleSpinner: Spinner

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
        val view = inflater.inflate(R.layout.fragment_route, container, false)

        view?.apply {
            requestPermissions()
            initializeView(this)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.routeList.observe(viewLifecycleOwner){
            if (it == null)
                return@observe
            else {
                adapter = RouteAdapter(requireContext(), it)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.hasFixedSize()

                /*
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        when (position) {
                            0 -> viewModel.getDataForSpinner("timestamp")
                            1 -> viewModel.getDataForSpinner("distance")
                            2 -> viewModel.getDataForSpinner("duration")
                            3 -> viewModel.getDataForSpinner("avgSpeed")
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
                */

                vehicleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val sortBy = spinner.selectedItemPosition
                        viewModel.getSortedRoutes(position, sortBy)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

               spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val vehicleType = vehicleSpinner.selectedItemPosition
                        viewModel.getSortedRoutes(vehicleType, position)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }



            }
        }

    }


    private fun initializeView(view: View) {
        recyclerView = view.findViewById(R.id.route_recyclerView)
        spinner = view.findViewById(R.id.route_filter)
        vehicleSpinner = view.findViewById(R.id.route_vehicleFilter)
    }

    private fun requestPermissions(){
        if (TrackingUtility.hasPermissions(requireContext()))
            return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "Accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            EasyPermissions.requestPermissions(
                this,
                "Accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
        else {
            EasyPermissions.requestPermissions(
                this,
                "Accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            AppSettingsDialog.Builder(this).build().show()
        else
            requestPermissions()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}