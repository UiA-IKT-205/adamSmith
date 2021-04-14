package com.example.todolistapp.activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistapp.R

class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        handler.postDelayed(runnable, 2000)
    }

    var runnable = Runnable {
        startNextActivity()
    }

    private fun startNextActivity() {
        startActivity(Intent(this, ListingActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        finish()
    }
}