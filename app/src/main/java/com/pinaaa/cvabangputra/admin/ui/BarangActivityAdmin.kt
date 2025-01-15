package com.pinaaa.cvabangputra.admin.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.adapter.BarangAdminAdapter
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.databinding.ActivityBarangAdminBinding

class BarangActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityBarangAdminBinding
    private lateinit var adapter: BarangAdminAdapter
    private val barangAdminViewModel: BarangAdminViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarangAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        barangAdminViewModel.fetchBarangs()

        setupSearchFeature()

        binding.btnBackBarangAdmin.setOnClickListener {
            finish()
        }

        binding.fabAddBarangAdmin.setOnClickListener {
            startActivity(Intent(this, AddBarangActivityAdmin::class.java))
        }


    }

    private fun setupRecyclerView() {
        adapter = BarangAdminAdapter()
        binding.rvBarangAdmin.layoutManager = GridLayoutManager(this, 2)
        binding.rvBarangAdmin.adapter = adapter
    }

    private fun observeViewModel() {
        barangAdminViewModel.barangList.observe(this) { barangList ->
            adapter.submitList(barangList)
        }

        barangAdminViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarBarangAdmin.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupSearchFeature() {
        binding.etSearchPencarianBarangAdmin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    barangAdminViewModel.searchBarangByName(query)
                } else {
                    barangAdminViewModel.fetchBarangs()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
