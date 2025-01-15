package com.pinaaa.cvabangputra

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pinaaa.cvabangputra.components.CustomDialogLoading
import com.pinaaa.cvabangputra.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var dialogLoading: CustomDialogLoading


    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }


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
        dialogLoading = CustomDialogLoading(this@RegisterActivity)

        binding.btnRegister.setOnClickListener {
            if (binding.etEmailRegister.text.toString().isEmpty() || binding.etPasswordRegister.text.toString().isEmpty() || binding.etNameRegister.text.toString().isEmpty() || binding.etNomorTeleponRegister.text.toString().isEmpty() || binding.etTangalLahirRegister.text.toString().isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("peringatan!")
                    .setMessage("Apakah data yang anda masukkan sudah benar?")
                    .setPositiveButton("Ya") { _, _ ->
                        dialogLoading.setLoadingVisible(true)
                        registerViewModel.addUsers(
                            binding.etEmailRegister.text.toString(),
                            binding.etPasswordRegister.text.toString(),
                            binding.etNameRegister.text.toString(),
                            binding.etNomorTeleponRegister.text.toString(),
                            binding.etTangalLahirRegister.text.toString()
                        )
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        registerViewModel.addUsersStatus.observe(this) { isRegistered ->
            if (isRegistered) {
                Toast.makeText(this, "register successfully", Toast.LENGTH_SHORT).show()
                dialogLoading.setLoadingVisible(false)
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            } else {
                dialogLoading.setLoadingVisible(false)
                Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT).show()
            }
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