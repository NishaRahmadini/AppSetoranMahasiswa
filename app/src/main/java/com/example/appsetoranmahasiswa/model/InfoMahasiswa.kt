package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class InfoMahasiswa(
    @SerializedName("nama") val nama: String?,
    @SerializedName("nim") val nim: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("angkatan") val angkatan: String?,
    @SerializedName("semester") val semester: Int?,
    @SerializedName("dosen_pa") val dosenPa: DosenPa?
)