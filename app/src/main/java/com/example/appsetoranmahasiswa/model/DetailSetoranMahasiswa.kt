package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class DetailSetoranMahasiswa(
    @SerializedName("info_dasar") val infoDasar: InfoDasarSetoran?,
    @SerializedName("ringkasan") val ringkasan: List<RingkasanSetoranItem>?,
    @SerializedName("detail") val detailSetoranList: List<ItemSetoranDetail>?
)