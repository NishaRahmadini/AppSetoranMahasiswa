package com.example.appsetoranmahasiswa.model

import com.google.gson.annotations.SerializedName

data class InfoDasarSetoran(
    @SerializedName("total_wajib_setor") val totalWajibSetor: Int?,
    @SerializedName("total_sudah_setor") val totalSudahSetor: Int?,
    @SerializedName("total_belum_setor") val totalBelumSetor: Int?,
    @SerializedName("persentase_progres_setor") val persentaseProgresSetor: Double?,
    @SerializedName("tgl_terakhir_setor") val tglTerakhirSetor: String?, // Ini adalah tanggal dalam format ISO 8601 (misalnya "2025-04-27T00:00:00.000Z")
    @SerializedName("terakhir_setor") val terakhirSetor: String? // Ini adalah string deskriptif (misalnya "Hari ini", "4 Hari yang lalu")
)