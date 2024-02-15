package com.example.routetrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()
        val iv_car = findViewById<ImageView>(R.id.iv_car)

        iv_car.alpha = 0f
        iv_car.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = if (auth.currentUser != null){
                Intent(this, MainActivity::class.java)
            }
            else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }

    }



}