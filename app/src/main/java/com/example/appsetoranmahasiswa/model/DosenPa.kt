package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class DosenPa(
    @SerializedName("nip") val nip: String?,
    @SerializedName("nama") val nama: String?,
    @SerializedName("email") val email: String?
)