package com.pinaaa.cvabangputra.reseller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.databinding.FragmentSearchResellerBinding
import com.pinaaa.cvabangputra.reseller.adapter.KategoriResellerAdapter
import com.pinaaa.cvabangputra.reseller.viewmodel.BerandaResellerViewModel

class SearchFragmentReseller : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentSearchResellerBinding? = null
    private val binding get() = _binding!!

    private val berandaResellerViewModel by viewModels<BerandaResellerViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeRefresh()
        setupRecyclerView()
        observeViewModel()

        // Load initial data
        berandaResellerViewModel.getKategori()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshSearchReseller.setOnRefreshListener(this)
    }

    private fun setupRecyclerView() {
        val kategoriResellerAdapter = KategoriResellerAdapter()
        binding.rvSearchReseller.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = kategoriResellerAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        berandaResellerViewModel.kategori.observe(viewLifecycleOwner) { kategoriList ->
            (binding.rvSearchReseller.adapter as KategoriResellerAdapter).submitList(kategoriList)
            binding.swipeRefreshSearchReseller.isRefreshing = false // Stop refresh indicator
            checkDataLoaded()
        }
    }

    private fun checkDataLoaded() {
        // Jika data sudah ada, sembunyikan progress bar
        if (berandaResellerViewModel.kategori.value != null) {
            binding.progressBarSearchReseller.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        // Refresh data saat swipe
        berandaResellerViewModel.getKategori()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
