package com.example.appsetoranmahasiswa.api

import com.example.appsetoranmahasiswa.auth.AuthService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiRetrofitInstance {

    private const val KC_URL = "https://id.tif.uin-suska.ac.id"
    const val API_BASE_URL = "https://api.tif.uin-suska.ac.id/setoran-dev/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val authRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(KC_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthService by lazy {
        authRetrofit.create(AuthService::class.java)
    }

    val apiService: ApiService by lazy {
        apiRetrofit.create(ApiService::class.java)
    }
}