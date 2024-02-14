package com.example.routetrack.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.routetrack.MainActivity
import com.example.routetrack.R
import com.example.routetrack.ui.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoginFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: UserViewModel by viewModels()
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth


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
        val view = inflater.inflate(R.layout.fragment_login, container, false)


        view?.apply{
            initializeView(this)
            registerListeners(this)
        }

        return view
    }

    private fun registerListeners(view: View) {
        loginButton.setOnClickListener {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty())
                Toast.makeText(activity,"Fill the missing fields!",Toast.LENGTH_SHORT).show()
            else{
                viewModel.login(email, password)
                viewModel.loginResponse.observe(viewLifecycleOwner, Observer { loginSuccess ->
                    if (loginSuccess == null)
                        return@Observer
                    else if (loginSuccess){
                        val intent = Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    else {
                        Toast.makeText(activity,"Invalid or expired credentials!",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun initializeView(view: View) {
        loginEmail = view.findViewById(R.id.emailInput)
        loginPassword = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)
        firebaseAuth = FirebaseAuth.getInstance()
    }


}