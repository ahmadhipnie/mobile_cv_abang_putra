package com.pinaaa.cvabangputra

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pinaaa.cvabangputra.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        playAnimationRegister()

    }

    private fun playAnimationRegister() {
        val durationAnimation: Long = 500
        val linearLayoutAnimation =
            ObjectAnimator.ofFloat(binding.llRegister, View.TRANSLATION_Y, 1000f, 0f).setDuration(1000)
        val tvEmailLoginAnimation = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f)
            .setDuration(durationAnimation)
        val etLayoutEmailLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutEmailRegister, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val etLayoutNameLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutNameRegister, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val etLayoutNomorTeleponLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutNomorTeleponRegister, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val etLayoutTanggalLahirLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutTangalLahirRegister, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val etLayoutPasswordLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutPasswordRegister, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val btnLupaPasswordAnimation =
            ObjectAnimator.ofFloat(binding.tvPersyaratanRegister, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val btnLoginAnimation =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(durationAnimation)
        val llRegisterLoginAnimation =
            ObjectAnimator.ofFloat(binding.llLoginRegister, View.ALPHA, 1f)
                .setDuration(durationAnimation)

        AnimatorSet().apply {
            playSequentially(
                linearLayoutAnimation,
                tvEmailLoginAnimation,
                etLayoutNameLoginAnimation,
                etLayoutEmailLoginAnimation,
                etLayoutPasswordLoginAnimation,
                etLayoutNomorTeleponLoginAnimation,
                etLayoutTanggalLahirLoginAnimation,
                btnLupaPasswordAnimation,
                btnLoginAnimation,
                llRegisterLoginAnimation
            )
            start()
        }
    }
}