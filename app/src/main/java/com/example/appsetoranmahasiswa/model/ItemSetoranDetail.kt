package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class ItemSetoranDetail(
    @SerializedName("id") val id: String?,
    @SerializedName("nama") val nama: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("sudah_setor") val sudahSetor: Boolean?,
    @SerializedName("info_setoran") val infoSetoran: InfoItemSetoran?
)