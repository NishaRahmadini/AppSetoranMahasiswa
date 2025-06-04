package com.example.appsetoranmahasiswa

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.appsetoranmahasiswa.api.ApiRetrofitInstance
import com.example.appsetoranmahasiswa.model.DataMahasiswaSetoran
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var textViewNamaMahasiswa: TextView
    private lateinit var textViewNimMahasiswa: TextView
    private lateinit var progressBarProgresSetoran: ProgressBar
    private lateinit var textViewPersentaseProgres: TextView
    private lateinit var textViewTerakhirSetor: TextView
    private lateinit var buttonDownloadPdf: Button
    private lateinit var buttonLogout: Button
    private lateinit var progressBarMain: ProgressBar
    private lateinit var toolbarMain: Toolbar

    // GANTI DENGAN API KEY ANDA YANG SEBENARNYA
    private val apiKey = "API_KEY_ANDA_DISINI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        supportActionBar?.title = "Dashboard Setoran"

        textViewNamaMahasiswa = findViewById(R.id.textViewNamaMahasiswa)
        textViewNimMahasiswa = findViewById(R.id.textViewNimMahasiswa)
        progressBarProgresSetoran = findViewById(R.id.progressBarProgresSetoran)
        textViewPersentaseProgres = findViewById(R.id.textViewPersentaseProgres)
        textViewTerakhirSetor = findViewById(R.id.textViewTerakhirSetor)
        buttonDownloadPdf = findViewById(R.id.buttonDownloadPdf)
        buttonLogout = findViewById(R.id.buttonLogout)
        progressBarMain = findViewById(R.id.progressBarMain)

        buttonLogout.setOnClickListener {
            performLogout()
        }

        buttonDownloadPdf.setOnClickListener {
            downloadRekapPdf()
        }

        fetchSetoranData()
    }

    private fun fetchSetoranData() {
        progressBarMain.visibility = View.VISIBLE

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)

        if (accessToken == null) {
            Toast.makeText(this, "Sesi berakhir, silakan login kembali.", Toast.LENGTH_LONG).show()
            performLogout()
            return
        }

        if (apiKey == "API_KEY_ANDA_DISINI" || apiKey.isBlank()) {
            Toast.makeText(this, "API Key belum dikonfigurasi.", Toast.LENGTH_LONG).show()
            progressBarMain.visibility = View.GONE
            return
        }

        lifecycleScope.launch {
            try {
                val response = ApiRetrofitInstance.apiService.getSetoranSaya(
                    authToken = "Bearer $accessToken",
                    apiKey = apiKey
                )

                progressBarMain.visibility = View.GONE
                if (response.isSuccessful) {
                    val setoranResponse = response.body()
                    if (setoranResponse != null && setoranResponse.response == true && setoranResponse.data != null) {
                        updateUI(setoranResponse.data)
                    } else {
                        Toast.makeText(this@MainActivity, "Gagal memuat data: ${setoranResponse?.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                progressBarMain.visibility = View.GONE
                Log.e("MainActivity", "Exception saat fetch data: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Gagal terhubung ke server: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(data: DataMahasiswaSetoran) {
        data.info?.let {
            textViewNamaMahasiswa.text = it.nama ?: "Nama tidak tersedia"
            textViewNimMahasiswa.text = "NIM: ${it.nim ?: "-"}"
        }

        data.setoran?.infoDasar?.let {
            progressBarProgresSetoran.progress = it.persentaseProgresSetor?.toInt() ?: 0
            textViewPersentaseProgres.text = String.format("%.2f%% (%d/%d)",
                it.persentaseProgresSetor ?: 0.0,
                it.totalSudahSetor ?: 0,
                it.totalWajibSetor ?: 0
            )
            textViewTerakhirSetor.text = "Terakhir setor: ${it.terakhirSetor ?: "-"}"
        }
    }

    private fun downloadRekapPdf() {
        progressBarMain.visibility = View.VISIBLE
        Toast.makeText(this, "Mulai mengunduh PDF...", Toast.LENGTH_SHORT).show()

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null)

        if (accessToken == null) {
            Toast.makeText(this, "Sesi berakhir, silakan login kembali.", Toast.LENGTH_LONG).show()
            performLogout()
            return
        }

        if (apiKey == "API_KEY_ANDA_DISINI" || apiKey.isBlank()) {
            Toast.makeText(this, "API Key belum dikonfigurasi.", Toast.LENGTH_LONG).show()
            progressBarMain.visibility = View.GONE
            return
        }

        lifecycleScope.launch {
            try {
                val response = ApiRetrofitInstance.apiService.downloadKartuMurojaah(
                    authToken = "Bearer $accessToken",
                    apiKey = apiKey
                )
                progressBarMain.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    // Simpan file menggunakan DownloadManager
                    val fileName = "rekap_setoran_${textViewNimMahasiswa.text.toString().replace("NIM: ", "")}.pdf"
                    val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                    val request = DownloadManager.Request(Uri.parse(ApiRetrofitInstance.API_BASE_URL + "mahasiswa/kartu-murojaah-saya?apikey=$apiKey")) // Perlu URL lengkap
                        .setTitle(fileName)
                        .setDescription("Mengunduh rekap setoran...")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                        .addRequestHeader("Authorization", "Bearer $accessToken") // Tambahkan header auth

                    downloadManager.enqueue(request)
                    Toast.makeText(this@MainActivity, "PDF sedang diunduh ke folder Downloads.", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this@MainActivity, "Gagal mengunduh PDF: ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                progressBarMain.visibility = View.GONE
                Log.e("MainActivity", "Exception saat download PDF: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Gagal mengunduh PDF: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun performLogout() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("ACCESS_TOKEN")
            apply()
        }
        // Panggil API logout Keycloak jika ada dan diperlukan

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Untuk menu refresh di Toolbar (opsional)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                fetchSetoranData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}