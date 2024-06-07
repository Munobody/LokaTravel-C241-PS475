package com.example.lokatravel.data.retrofit

import com.example.lokatravel.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            // Interceptor untuk logging HTTP request dan response body
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }

            // Membuat OkHttpClient dengan interceptor yang sudah diset
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                // Tambahkan interceptor lain di sini sesuai kebutuhan
                .build()

            // Membuat Retrofit instance dengan konfigurasi baseUrl dan converter factory
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            // Mengembalikan ApiService yang dibuat oleh Retrofit
            return retrofit.create(ApiService::class.java)
        }
    }
}
