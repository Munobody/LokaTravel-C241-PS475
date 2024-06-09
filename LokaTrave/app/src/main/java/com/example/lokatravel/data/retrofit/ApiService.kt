package com.example.lokatravel.data.retrofit
import com.example.lokatravel.data.response.LoginResponse
import com.example.lokatravel.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("/signup")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>
}

