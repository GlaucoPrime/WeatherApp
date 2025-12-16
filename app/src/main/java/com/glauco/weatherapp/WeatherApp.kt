package com.glauco.weatherapp

import android.app.Application
import android.content.Intent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WeatherApp : Application() {
    val FLAGS = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    override fun onCreate() {
        super.onCreate()
        Firebase.auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                goToMain()
            } else {
                goToLogin()
            }
        }
    }

    private fun goToMain() {
        this.startActivity(Intent(this, MainActivity::class.java).setFlags(FLAGS))
    }

    private fun goToLogin() {
        this.startActivity(Intent(this, LoginActivity::class.java).setFlags(FLAGS))
    }
}