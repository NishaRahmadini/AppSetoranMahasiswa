package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class RingkasanSetoranItem(
    @SerializedName("label") val label: String?,
    @SerializedName("total_wajib_setor") val totalWajibSetor: Int?,
    @SerializedName("total_sudah_setor") val totalSudahSetor: Int?,
    @SerializedName("total_belum_setor") val totalBelumSetor: Int?,
    @SerializedName("persentase_progres_setor") val persentaseProgresSetor: Double?
)