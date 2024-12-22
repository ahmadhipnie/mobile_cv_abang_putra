package com.pinaaa.cvabangputra.reseller.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.databinding.ActivityMainResellerBinding
import com.pinaaa.cvabangputra.reseller.fragment.BerandaFragmentReseller
import com.pinaaa.cvabangputra.reseller.fragment.FavoritFragmentReseller
import com.pinaaa.cvabangputra.reseller.fragment.ProfilFragmentReseller
import com.pinaaa.cvabangputra.reseller.fragment.SearchFragmentReseller

class MainActivityReseller : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    // Deklarasi fragment
    var berandaFragmentReseller : BerandaFragmentReseller = BerandaFragmentReseller()
    var profilFragmentReseller: ProfilFragmentReseller = ProfilFragmentReseller()
    var searchFragmentReseller: SearchFragmentReseller = SearchFragmentReseller()
    var favoritFragmentReseller: FavoritFragmentReseller = FavoritFragmentReseller()



    private lateinit var binding: ActivityMainResellerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainResellerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Inisialisasi BottomNavigationView
        binding.bottomNavigationViewReseller.apply {
            setOnNavigationItemSelectedListener(this@MainActivityReseller)

            // Mengecek ekstra "tab" yang diterima dari intent untuk menentukan tab yang akan dibuka
            val tabToOpen = intent.getStringExtra("tab")
            if (tabToOpen != null) {
                when (tabToOpen) {
                    "profile" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, profilFragmentReseller)
                            .commit()
                        selectedItemId = R.id.menu_profil_reseller
                    }
                    "search" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, searchFragmentReseller)
                            .commit()
                        selectedItemId = R.id.menu_search_reseller
                    }
                    "favorit" -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, favoritFragmentReseller)
                            .commit()
                        selectedItemId = R.id.menu_search_reseller
                    }
//                    "beranda" -> {
//                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, berandaFragmentReseller)
//                            .commit()
//                        selectedItemId = R.id.menu_beranda_reseller
//                    }
                    else -> {
                        // Buka tab beranda sebagai default
                        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, berandaFragmentReseller)
                            .commit()
                    }
                }
            } else {
                // Buka tab beranda sebagai default jika tidak ada ekstra "tab"
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, berandaFragmentReseller)
                    .commit()
            }
        }
    }

    // Metode yang dipanggil saat item navigasi dipilih
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        // Tampilkan fragment yang sesuai berdasarkan item yang dipilih
        when (itemId) {
            R.id.menu_beranda_reseller -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, berandaFragmentReseller)
                    .commit()
                return true
            }
            R.id.menu_profil_reseller -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, profilFragmentReseller)
                    .commit()
                return true
            }
            R.id.menu_favorite_reseller -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, favoritFragmentReseller)
                    .commit()
                return true
            }
            R.id.menu_search_reseller -> {
                supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_reseller, searchFragmentReseller)
                    .commit()
                return true
            }
        }
        return false
    }
}