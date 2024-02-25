package com.example.routetrack.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.routetrack.R
import com.example.routetrack.ui.viewmodels.SummaryViewModel
import com.example.routetrack.utility.TrackingUtility


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SummaryFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: SummaryViewModel by viewModels()
    private lateinit var monthlyDistance: TextView
    private lateinit var monthlyTime: TextView
    private lateinit var monthlyFuel: TextView
    private lateinit var monthlyFuelCost: TextView
    private lateinit var spinner: Spinner

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
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        view?.apply {
            initializeView(this)
        }

        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        viewModel.getMonthlyDistance()
                        viewModel.monthlyDistance.observe(viewLifecycleOwner) {
                            if (it == null){
                                return@observe
                            }
                            else {
                                monthlyDistance.text = String.format("%.2f km", it)
                            }
                        }

                        viewModel.getMonthlyTime()
                        viewModel.monthlyTime.observe(viewLifecycleOwner){
                            if (it == null){
                                return@observe
                            }
                            else{
                                monthlyTime.text = TrackingUtility.formatTime(it, false)
                            }
                        }
                    }
                    1 -> {
                        viewModel.getMonthlyDistanceVehicle(0)
                        viewModel.monthlyDistance.observe(viewLifecycleOwner) {
                            if (it == null){
                                return@observe
                            }
                            else {
                                monthlyDistance.text = String.format("%.2f km", it)
                            }
                        }

                        viewModel.getMonthlyTimeVehicle(0)
                        viewModel.monthlyTime.observe(viewLifecycleOwner){
                            if (it == null){
                                return@observe
                            }
                            else{
                                monthlyTime.text = TrackingUtility.formatTime(it, false)
                            }
                        }
                    }
                    2 -> {
                        viewModel.getMonthlyDistanceVehicle(1)
                        viewModel.monthlyDistance.observe(viewLifecycleOwner) {
                            if (it == null){
                                return@observe
                            }
                            else {
                                monthlyDistance.text = String.format("%.2f km", it)
                            }
                        }

                        viewModel.getMonthlyTimeVehicle(1)
                        viewModel.monthlyTime.observe(viewLifecycleOwner){
                            if (it == null){
                                return@observe
                            }
                            else{
                                monthlyTime.text = TrackingUtility.formatTime(it, false)
                            }
                        }
                    }
                    3 -> {
                        viewModel.getMonthlyDistanceVehicle(2)
                        viewModel.monthlyDistance.observe(viewLifecycleOwner) {
                            if (it == null){
                                return@observe
                            }
                            else {
                                monthlyDistance.text = String.format("%.2f km", it)
                            }
                        }

                        viewModel.getMonthlyTimeVehicle(2)
                        viewModel.monthlyTime.observe(viewLifecycleOwner){
                            if (it == null){
                                return@observe
                            }
                            else{
                                monthlyTime.text = TrackingUtility.formatTime(it, false)
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }


        viewModel.getMonthlyFuel()
        viewModel.monthlyFuel.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            else
                monthlyFuel.text = String.format("%.2f L", it)
        }

        viewModel.getMonthlyFuelCost()
        viewModel.monthlyFuelCost.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe
            else
                monthlyFuelCost.text = String.format("%.2f RON", it)
        }

    }


    private fun initializeView(view: View) {
        monthlyDistance = view.findViewById(R.id.totalDistance)
        monthlyTime = view.findViewById(R.id.totalTime)
        monthlyFuel = view.findViewById(R.id.totalFuel)
        monthlyFuelCost = view.findViewById(R.id.totalFuelCost)
        spinner = view.findViewById(R.id.summary_vehicleSpinner)
    }


}