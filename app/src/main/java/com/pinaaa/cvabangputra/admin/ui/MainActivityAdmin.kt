package com.pinaaa.cvabangputra.admin.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.admin.fragment.BerandaFragmentAdmin
import com.pinaaa.cvabangputra.admin.fragment.FeedbackFragmentAdmin
import com.pinaaa.cvabangputra.admin.fragment.LaporanFragmentAdmin
import com.pinaaa.cvabangputra.admin.fragment.ProfilFragmentAdmin
import com.pinaaa.cvabangputra.databinding.ActivityMainAdminBinding

class MainActivityAdmin : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainAdminBinding

    var berandaFragmentAdmin: BerandaFragmentAdmin = BerandaFragmentAdmin()
    var laporanFragmentAdmin: LaporanFragmentAdmin = LaporanFragmentAdmin()
    var feedbackFragmentAdmin: FeedbackFragmentAdmin = FeedbackFragmentAdmin()
    var profilFragmentAdmin: ProfilFragmentAdmin = ProfilFragmentAdmin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavigationViewAdmin.apply {
            setOnNavigationItemSelectedListener(this@MainActivityAdmin)

            // Mengecek ekstra "tab" yang diterima dari intent untuk menentukan tab yang akan dibuka
            val tabToOpen = intent.getStringExtra("tab")
            if (tabToOpen != null) {
                when (tabToOpen) {
                    "profile" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, profilFragmentAdmin)
                            .commit()
                        selectedItemId = R.id.menu_profil_admin
                    }
                    "laporan" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, laporanFragmentAdmin)
                            .commit()
                        selectedItemId = R.id.menu_laporan_admin
                    }
                    "feedback" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, feedbackFragmentAdmin)
                            .commit()
                        selectedItemId = R.id.menu_feedback_admin
                    }
//                    "beranda" -> {
//                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, berandaFragmentAdmin)
//                            .commit()
//                        selectedItemId = R.id.menu_beranda_admin
//                    }
                    else -> {
                        // Buka tab beranda sebagai default
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, berandaFragmentAdmin)
                            .commit()
                    }
                }
            } else {
                // Buka tab beranda sebagai default jika tidak ada ekstra "tab"
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, berandaFragmentAdmin)
                    .commit()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        // Tampilkan fragment yang sesuai berdasarkan item yang dipilih
        when (itemId) {
            R.id.menu_beranda_admin -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, berandaFragmentAdmin)
                    .commit()
                return true
            }
            R.id.menu_profil_admin -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, profilFragmentAdmin)
                    .commit()
                return true
            }
            R.id.menu_laporan_admin -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, laporanFragmentAdmin)
                    .commit()
                return true
            }
            R.id.menu_feedback_admin -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_admin, feedbackFragmentAdmin)
                    .commit()
                return true
            }
        }
        return false
    }
}