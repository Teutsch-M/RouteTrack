package com.example.routetrack.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.routetrack.LoginActivity
import com.example.routetrack.R
import com.example.routetrack.ui.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SettingsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: UserViewModel by viewModels()
    private lateinit var identifierText: TextView
    private lateinit var logoutButton: Button

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
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        view.apply {
            initializeView(this)
            registerListeners(this)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val identifier = FirebaseAuth.getInstance().currentUser!!.displayName
        if (identifier != null)
            identifierText.text = identifier
        else
            identifierText.text = FirebaseAuth.getInstance().currentUser!!.email


    }

    private fun initializeView(view: View) {
        identifierText = view.findViewById(R.id.identifierText)
        logoutButton = view.findViewById(R.id.signOutButton)
    }

    private fun registerListeners(view: View) {
        logoutButton.setOnClickListener {
            viewModel.logout { success ->
                if (success){
                    val intent = Intent(this@SettingsFragment.requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                else {
                    Toast.makeText(activity, "Logout failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}