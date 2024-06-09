package com.example.lokatravel.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.example.lokatravel.data.retrofit.ApiConfig
import com.example.lokatravel.data.response.LoginResponse
import com.example.lokatravel.data.retrofit.LoginRequest
import com.example.lokatravel.data.retrofit.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun loginUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Email and password are required"
            return
        }

        val apiService = ApiConfig.getApiService()
        val requestBody = LoginRequest(email = email, password = password)
        val loginCall = apiService.login(requestBody)

        Log.d("LoginViewModel", "Logging in with email: $email")

        loginCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _loginResponse.value = it
                    } ?: run {
                        _error.value = "Empty response body"
                    }
                } else {
                    _error.value = "Login failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.value = "Login failed: ${t.message}"
            }
        })
    }
}