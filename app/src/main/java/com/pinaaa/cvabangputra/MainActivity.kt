package com.pinaaa.cvabangputra

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pinaaa.cvabangputra.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Display splash screen for a while (1 second here)
        Handler().postDelayed({
            if (onBoardingIsFinished()) {
                // After the splash screen, open LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)  // Fade-in and fade-out transition
            } else {
                // After the splash screen, open OnboardingActivity
                startActivity(Intent(this, OnboardingActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)  // Fade-in and fade-out transition
            }
            finish()  // Close MainActivity after transition
        }, 1000)  // 1000 ms = 1 second
    }

    // Function to check if onboarding is finished
    private fun onBoardingIsFinished(): Boolean {
        val sharedPreferences = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("finished", false)  // Check if 'finished' is true
    }
}
