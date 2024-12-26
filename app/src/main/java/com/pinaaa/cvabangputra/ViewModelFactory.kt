package com.pinaaa.cvabangputra

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinaaa.cvabangputra.di.injection
import com.pinaaa.cvabangputra.reseller.viewmodel.BerandaResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailBarangResellerViewModel

class ViewModelFactory private constructor(private val repository: Repository): ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BerandaResellerViewModel::class.java) -> {
                BerandaResellerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailBarangResellerViewModel::class.java) -> {
                DetailBarangResellerViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }


    companion object {
        fun getInstance(context: Context) = ViewModelFactory(injection.provideRepository(context))
    }
}