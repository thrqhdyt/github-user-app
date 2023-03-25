package com.thorcode.githubuserapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.thorcode.githubuserapp.databinding.ActivitySplashBinding


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val handler = Handler()
        handler.postDelayed({
            val intentSplash = Intent(this@SplashActivity, MainActivity::class.java)
            intentSplash.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intentSplash)
            finish()
        }, DELAY_TIME)
    }

    companion object {
        private const val DELAY_TIME = 2000L
    }
}