package com.pinaaa.cvabangputra.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinaaa.cvabangputra.data.remote.response.admin.DataFeedbackItem
import com.pinaaa.cvabangputra.databinding.FeedbackItemBinding
import com.pinaaa.cvabangputra.di.injection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackAdminAdapter : RecyclerView.Adapter<FeedbackAdminAdapter.FeedbackAdminViewHolder>() {
    private var dataList = emptyList<DataFeedbackItem>()


    fun setData(data: List<DataFeedbackItem>) {
        dataList = data
        notifyDataSetChanged()
    }

    inner class FeedbackAdminViewHolder(val binding: FeedbackItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataFeedbackItem) {

            binding.apply {
                tvItemEmailReseller.text = item.email
                tvItemIsiFeedback.text = item.isiFeedback
                ratingFeedbackAdmin.rating = item.rating.toFloat()
                tvCreatedAt.text = formatTanggal(item.createdAt)

            }
        }

        // Fungsi untuk memformat tanggal
        private fun formatTanggal(dateString: String): String {
            return try {
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = inputDateFormat.parse(dateString)
                val outputDateFormat = SimpleDateFormat("dd - MMMM - yyyy", Locale("id", "ID"))
                outputDateFormat.format(date ?: Date())
            } catch (e: Exception) {
                dateString // Jika gagal memformat, gunakan string asli
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackAdminViewHolder {
        val binding =
            FeedbackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedbackAdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackAdminViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}
