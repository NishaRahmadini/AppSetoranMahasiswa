package com.example.appsetoranmahasiswa.auth

import com.example.appsetoranmahasiswa.TokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AuthService {
    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token")
    suspend fun login(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
        @Field("scope") scope: String = "openid profile email"
    ): Response<TokenResponse>
}