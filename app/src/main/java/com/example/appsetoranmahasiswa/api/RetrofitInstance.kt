package com.example.appsetoranmahasiswa.api

import com.example.appsetoranmahasiswa.auth.AuthService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val KC_BASE_URL = "https://id.tif.uin-suska.ac.id/" // URL dasar Keycloak Anda

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Menampilkan body request dan response, berguna untuk debugging
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Tambahkan interceptor logging
        .build()

    val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(KC_BASE_URL)
            .client(client) // Gunakan OkHttpClient yang sudah dikonfigurasi
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}