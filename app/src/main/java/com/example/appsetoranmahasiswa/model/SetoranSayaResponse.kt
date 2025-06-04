package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class SetoranSayaResponse(
    @SerializedName("response") val response: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: DataMahasiswaSetoran?
)