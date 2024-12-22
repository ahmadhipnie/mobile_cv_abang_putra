package com.pinaaa.cvabangputra

import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.response.LoginResponse

class LoginViewModel(private val repository: Repository) : ViewModel() {

    suspend fun login(email:String?, password:String?): LoginResponse {
        return   repository.login(email!!,password!!)
    }
}