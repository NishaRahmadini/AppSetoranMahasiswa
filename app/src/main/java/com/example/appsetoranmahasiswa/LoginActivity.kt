package com.example.appsetoranmahasiswa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.appsetoranmahasiswa.api.RetrofitInstance
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var progressBarLogin: ProgressBar

    private val clientId = "/setoran-dev/v1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        progressBarLogin = findViewById(R.id.progressBarLogin)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.authService.login(
                    clientId = clientId,
                    username = username,
                    password = password
                )

                showLoading(false)

                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse != null && tokenResponse.accessToken != null) {
                        Log.d("LoginActivity", "Access Token: ${tokenResponse.accessToken}")
                        Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_LONG).show()

                    } else if (tokenResponse != null && tokenResponse.error != null) {
                        Log.e("LoginActivity", "Error Keycloak: ${tokenResponse.error} - ${tokenResponse.errorDescription}")
                        Toast.makeText(this@LoginActivity, "Login Gagal: ${tokenResponse.errorDescription ?: tokenResponse.error}", Toast.LENGTH_LONG).show()
                    } else {
                        Log.e("LoginActivity", "Respons berhasil tapi tidak ada token atau error.")
                        Toast.makeText(this@LoginActivity, "Login Gagal: Respons tidak valid.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginActivity", "Login Gagal (HTTP Error): ${response.code()} - $errorBody")
                    try {
                        val errorResponse = com.google.gson.Gson().fromJson(errorBody, TokenResponse::class.java)
                        val errorMessage = errorResponse?.errorDescription ?: errorResponse?.error ?: "Terjadi kesalahan jaringan"
                        Toast.makeText(this@LoginActivity, "Login Gagal: $errorMessage", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@LoginActivity, "Login Gagal: Terjadi kesalahan jaringan (${response.code()})", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                showLoading(false)
                Log.e("LoginActivity", "Exception saat login: ${e.message}", e)
                Toast.makeText(this@LoginActivity, "Login Gagal: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
        buttonLogin.isEnabled = !isLoading
        editTextUsername.isEnabled = !isLoading
        editTextPassword.isEnabled = !isLoading
    }
}