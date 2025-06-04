package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class DataMahasiswaSetoran(
    @SerializedName("info") val info: InfoMahasiswa?,
    @SerializedName("setoran") val setoran: DetailSetoranMahasiswa?
)