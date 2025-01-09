package com.pinaaa.cvabangputra

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.pinaaa.cvabangputra.admin.ui.MainActivityAdmin
import com.pinaaa.cvabangputra.components.CustomDialogLoading
import com.pinaaa.cvabangputra.databinding.ActivityLoginBinding
import com.pinaaa.cvabangputra.reseller.ui.MainActivityReseller
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var dialogLoading: CustomDialogLoading

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)


        playAnimationLogin()

        binding.btnRegisterLogin.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLogin.setOnClickListener {
            dialogLoading = CustomDialogLoading(this@LoginActivity)
            dialogLoading.setLoadingVisible(true)

            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()

            lifecycleScope.launch {
                try {
                    val response = loginViewModel.login(email, password)
                    if (response.role == "admin") {

                        val editor = sharedPreferences.edit()
                        editor.putString("email", email)
                        editor.putString("password", password)
                        editor.putString("role", response.role)
                        editor.putBoolean("isLoggedInAdmin", true)
                        editor.apply()

                        dialogLoading.setLoadingVisible(false)
                        Toast.makeText(
                            this@LoginActivity,
                            "anda login sebagai admin",
                            Toast.LENGTH_SHORT
                        ).show()


                        Intent(this@LoginActivity, MainActivityAdmin::class.java).apply {
                            startActivity(this)
                            finish()
                        }

                    } else if (response.role == "reseller") {

                        val editor = sharedPreferences.edit()
                        editor.putString("email", email)
                        editor.putString("password", password)
                        editor.putString("role", response.role)
                        editor.putString("id_reseller", response.data?.idReseller.toString())
                        editor.putString("user_id", response.data?.userId.toString())
                        editor.putString("nama", response.data?.nama.toString())
                        editor.putString("alamat", response.data?.alamat.toString())
                        editor.putString("nomor_telepon", response.data?.nomorTelepon.toString())
                        editor.putString("tanggal_lahir", response.data?.tanggalLahir.toString())
                        editor.putString("foto_profil", response.data?.fotoProfil.toString())
                        editor.putBoolean("isLoggedInReseller", true)
                        editor.apply()

                        dialogLoading.setLoadingVisible(false)
                        Toast.makeText(
                            this@LoginActivity,
                            "anda login sebagai reseller",
                            Toast.LENGTH_SHORT
                        ).show()

                        Intent(this@LoginActivity, MainActivityReseller::class.java).apply {
                            startActivity(this)
                            finish()
                        }
                    }
                } catch (e: HttpException) {
                    dialogLoading.setLoadingVisible(false)
                    Toast.makeText(
                        this@LoginActivity,
                        "Terjadi kesalahan, silahkan coba lagi",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.e(TAG, "onCreate: ", e)
                }

            }


        }


    }

    private fun playAnimationLogin() {
        val durationAnimation: Long = 500
        val linearLayoutAnimation =
            ObjectAnimator.ofFloat(binding.llLogin, View.TRANSLATION_Y, 1000f, 0f).setDuration(1000)
        val tvEmailLoginAnimation = ObjectAnimator.ofFloat(binding.tvEmailLogin, View.ALPHA, 1f)
            .setDuration(durationAnimation)
        val etLayoutEmailLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutEmailLogin, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val etLayoutPasswordLoginAnimation =
            ObjectAnimator.ofFloat(binding.etLayoutPasswordLogin, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val btnLupaPasswordAnimation =
            ObjectAnimator.ofFloat(binding.btnLupaPassword, View.ALPHA, 1f)
                .setDuration(durationAnimation)
        val btnLoginAnimation =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(durationAnimation)
        val llRegisterLoginAnimation =
            ObjectAnimator.ofFloat(binding.llRegisterLogin, View.ALPHA, 1f)
                .setDuration(durationAnimation)

        AnimatorSet().apply {
            playSequentially(
                linearLayoutAnimation,
                tvEmailLoginAnimation,
                etLayoutEmailLoginAnimation,
                etLayoutPasswordLoginAnimation,
                btnLupaPasswordAnimation,
                btnLoginAnimation,
                llRegisterLoginAnimation
            )
            start()
        }
    }
}