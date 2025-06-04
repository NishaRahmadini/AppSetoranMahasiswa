package com.example.appsetoranmahasiswa.api

import com.example.appsetoranmahasiswa.model.SetoranSayaResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ApiService {

    @GET("mahasiswa/setoran-saya")
    suspend fun getSetoranSaya(
        @Header("Authorization") authToken: String,
        @Query("apikey") apiKey: String
    ): Response<SetoranSayaResponse>

    @Streaming
    @GET("mahasiswa/kartu-murojaah-saya")
    suspend fun downloadKartuMurojaah(
        @Header("Authorization") authToken: String,
        @Query("apikey") apiKey: String
    ): Response<ResponseBody>
}