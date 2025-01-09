package com.pinaaa.cvabangputra.admin.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.adapter.FeedbackAdminAdapter
import com.pinaaa.cvabangputra.admin.viewmodel.FeedbackAdminViewModel
import com.pinaaa.cvabangputra.databinding.FragmentFeedbackAdminBinding

class FeedbackFragmentAdmin : Fragment() {
    private var _binding : FragmentFeedbackAdminBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: FeedbackAdminAdapter

    private val feedbackAdminViewModel by viewModels<FeedbackAdminViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFeedbackAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FeedbackAdminAdapter()

        feedbackAdminViewModel.feedbackList.observe(viewLifecycleOwner) { feedbackList ->
            if (feedbackList.isNotEmpty()) {
                showLoading(true)
                adapter.setData(feedbackList)
                showLoading(false)
            } else {
                Log.e("FeedbackFragment", "Feedback list kosong")
            }
        }

        binding.rvFeedbackAdmin.layoutManager = LinearLayoutManager(context)
        binding.rvFeedbackAdmin.adapter = adapter
        feedbackAdminViewModel.fetchFeedbacks()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFeedbackAdmin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }






}