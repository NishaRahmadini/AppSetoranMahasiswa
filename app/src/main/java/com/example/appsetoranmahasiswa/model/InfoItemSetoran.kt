package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class InfoItemSetoran(
    @SerializedName("id") val idSetoran: String?,
    @SerializedName("tgl_setoran") val tglSetoran: String?,
    @SerializedName("tgl_validasi") val tglValidasi: String?,
    @SerializedName("dosen_yang_mengesahkan") val dosenPengesah: DosenPa?
)
