package com.example.lokatravel.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lokatravel.data.retrofit.ApiConfig
import com.example.lokatravel.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse>
        get() = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun registerUser(fullname: String, email: String, password: String, confirmPassword: String) {
        if (fullname.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _error.value = "All fields are required"
            return
        }

        if (password != confirmPassword) {
            _error.value = "Passwords do not match"
            return
        }

        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val registerCall = apiService.register(fullname, email, password, confirmPassword)

        registerCall.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registerResponse.value = it
                    } ?: run {
                        _error.value = "Empty response body"
                    }
                } else {
                    if (response.code() == 503) {
                        _error.value = "Service is unavailable, please try again later."
                    } else {
                        _error.value = "Registration failed: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = "Registration failed: ${t.message}"
            }
        })
    }
}
