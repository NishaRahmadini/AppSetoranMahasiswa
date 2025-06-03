package com.example.appsetoranmahasiswa

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    @SerializedName("refresh_expires_in")
    val refreshExpiresIn: Int?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("id_token")
    val idToken: String?,
    @SerializedName("not-before-policy")
    val notBeforePolicy: Int?,
    @SerializedName("session_state")
    val sessionState: String?,
    @SerializedName("scope")
    val scope: String?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("error_description")
    val errorDescription: String?
)