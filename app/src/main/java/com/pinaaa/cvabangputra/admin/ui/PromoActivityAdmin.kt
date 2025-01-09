package com.pinaaa.cvabangputra.admin.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.adapter.BarangAdminAdapter
import com.pinaaa.cvabangputra.admin.adapter.PromoAdminAdapter
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.PromoAdminViewModel
import com.pinaaa.cvabangputra.databinding.ActivityDetailBarangAdminBinding
import com.pinaaa.cvabangputra.databinding.ActivityPromoAdminBinding

class PromoActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityPromoAdminBinding

    private lateinit var adapter: PromoAdminAdapter
    private val promoAdminViewModel: PromoAdminViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPromoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeViewModel()
        promoAdminViewModel.fetchPromos()

        setupSearchFeature()

        binding.btnBackPromoAdmin.setOnClickListener {
            finish()
        }


    }

    private fun setupRecyclerView() {
        adapter = PromoAdminAdapter()
        binding.rvPromoAdmin.layoutManager = GridLayoutManager(this, 1)
        binding.rvPromoAdmin.adapter = adapter
    }

    private fun observeViewModel() {
        promoAdminViewModel.promoList.observe(this) { barangList ->
            adapter.submitList(barangList)
        }

        promoAdminViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarPromoAdmin.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupSearchFeature() {
        binding.etSearchPencarianPromoAdmin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    promoAdminViewModel.searchPromoByName(query)
                } else {
                    promoAdminViewModel.fetchPromos()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}