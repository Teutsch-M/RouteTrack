package com.example.routetrack.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.google.firebase.auth.FirebaseAuth

class UserViewModel: ViewModel() {

    private val TAG = "UserViewModel"
    private val auth = FirebaseAuth.getInstance()
    private val _loginResponse = MutableLiveData<Boolean?>()
    val loginResponse: LiveData<Boolean?> = _loginResponse.distinctUntilChanged()

    fun login(email: String, password: String) {
        _loginResponse.value = null
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {  task ->
                if (task.isSuccessful) {
                    _loginResponse.postValue(true)
                }
                else {
                    _loginResponse.postValue(false)
                }
            }
    }

    fun logout(onLogoutComplete: (Boolean) -> Unit) {
        auth.signOut()
        val isLoggedOut = auth.currentUser == null
        onLogoutComplete(isLoggedOut)
    }

}