package com.example.routetrack.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.routetrack.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RefuelFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var addRefuel: ExtendedFloatingActionButton


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
        val view = inflater.inflate(R.layout.fragment_refuel, container, false)

        view.apply {
            initializeView(this)
            registerListeners(this)
        }

        return view
    }


    private fun initializeView(view: View) {
        recyclerView = view.findViewById(R.id.refuel_recyclerView)
        addRefuel = view.findViewById(R.id.addRefuelFloatingButton)
    }

    private fun registerListeners(view: View) {
        addRefuel.setOnClickListener {
            findNavController().navigate(R.id.action_refuelFragment_to_addRefuelFragment)
        }
    }

}