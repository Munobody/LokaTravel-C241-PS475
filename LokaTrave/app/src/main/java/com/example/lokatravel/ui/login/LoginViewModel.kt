package com.example.lokatravel.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lokatravel.data.retrofit.ApiConfig
import com.example.lokatravel.data.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Email or Password cannot be empty"
            return
        }

        _isLoading.value = true

        ApiConfig.getApiService().login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _loginResponse.value = it
                    } ?: run {
                        _error.value = "Empty response body"
                    }
                } else {
                    when (response.code()) {
                        503 -> _error.value = "Service is unavailable, please try again later."
                        else -> _error.value = "Login failed: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = "Login failed: ${t.message}"
            }
        })
    }
}
