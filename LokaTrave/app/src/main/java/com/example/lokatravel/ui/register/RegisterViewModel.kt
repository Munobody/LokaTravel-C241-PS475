package com.example.lokatravel.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lokatravel.data.response.RegisterResponse
import com.example.lokatravel.data.retrofit.ApiConfig
import com.example.lokatravel.data.retrofit.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse>
        get() = _registerResponse

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

        val apiService = ApiConfig.getFirstApiService()
        val requestBody = RegisterRequest(fullname, email, password, confirmPassword)
        val registerCall = apiService.register(requestBody)

        Log.d("RegisterViewModel", "Registering user with data: fullname=$fullname, email=$email, password=$password, confirmPassword=$confirmPassword")

        registerCall.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registerResponse.value = it
                    } ?: run {
                        _error.value = "Empty response body"
                    }
                } else {
                    _error.value = "Registration failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _error.value = "Registration failed: ${t.message}"
            }
        })
    }
}
