package com.example.routetrack.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var monthlyDistance: TextView
    private lateinit var monthlyTime: TextView
    private val viewModel: SummaryViewModel by viewModels()

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

        viewModel.getMonthlyDistance()
        viewModel.monthlyDistance.observe(viewLifecycleOwner) {
            if (it == null){
                return@observe
            }
            else {
                monthlyDistance.text = "$it km"
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


    private fun initializeView(view: View) {
        monthlyDistance = view.findViewById(R.id.totalDistance)
        monthlyTime = view.findViewById(R.id.totalTime)
    }


}