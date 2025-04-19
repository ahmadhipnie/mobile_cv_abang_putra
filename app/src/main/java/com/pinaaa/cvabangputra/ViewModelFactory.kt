package com.pinaaa.cvabangputra

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.FeedbackAdminViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.PromoAdminViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.StokBarangViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.UbahPasswordAdminViewModel
import com.pinaaa.cvabangputra.di.injection
import com.pinaaa.cvabangputra.reseller.viewmodel.BerandaResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailBarangResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailPromoResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.FavoriteResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.KirimFeedbackResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.PencarianBarangResellerViewModel

class ViewModelFactory private constructor(private val repository: Repository): ViewModelProvider.NewInstanceFactory()  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BarangAdminViewModel::class.java) -> {
                BarangAdminViewModel() as T
            }
            modelClass.isAssignableFrom(PromoAdminViewModel::class.java) -> {
                PromoAdminViewModel() as T
            }
            modelClass.isAssignableFrom(StokBarangViewModel::class.java) -> {
                StokBarangViewModel() as T
            }
            modelClass.isAssignableFrom(UbahPasswordAdminViewModel::class.java) -> {
                UbahPasswordAdminViewModel() as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel() as T
            }
            modelClass.isAssignableFrom(FeedbackAdminViewModel::class.java) -> {
                FeedbackAdminViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BerandaResellerViewModel::class.java) -> {
                BerandaResellerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailBarangResellerViewModel::class.java) -> {
                DetailBarangResellerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PencarianBarangResellerViewModel::class.java) -> {
                PencarianBarangResellerViewModel() as T
            }
            modelClass.isAssignableFrom(DetailPromoResellerViewModel::class.java) -> {
                DetailPromoResellerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(KirimFeedbackResellerViewModel::class.java) -> {
                KirimFeedbackResellerViewModel(repository) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }


    companion object {
        fun getInstance(context: Context) = ViewModelFactory(injection.provideRepository(context))
    }
}