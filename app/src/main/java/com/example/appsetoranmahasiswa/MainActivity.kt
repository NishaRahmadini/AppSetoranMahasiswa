package com.example.appsetoranmahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textViewWelcome: TextView
    private lateinit var textViewTokenInfo: TextView
    private lateinit var buttonLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewWelcome = findViewById(R.id.textViewWelcome)
        textViewTokenInfo = findViewById(R.id.textViewTokenInfo)
        buttonLogout = findViewById(R.id.buttonLogout)

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)

        if (accessToken != null) {
            textViewTokenInfo.text = "Login berhasil (token ada)"
        } else {
            textViewTokenInfo.text = "Token tidak ditemukan. Silakan login kembali."

        }

        buttonLogout.setOnClickListener {
            with(sharedPreferences.edit()) {
                remove("ACCESS_TOKEN")
                apply()
            }

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}