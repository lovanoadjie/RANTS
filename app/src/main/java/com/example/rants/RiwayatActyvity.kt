package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rants.adapter.RiwayatAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.RiwayatActyvityBinding
import com.example.rants.model.PesananKostumResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActyvity : AppCompatActivity() {
    private lateinit var binding: RiwayatActyvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        binding = RiwayatActyvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.bottom_riwayat

        setupBottomNavigation()
        setupRecyclerView() // Panggil fungsi untuk ambil riwayat
    }

    private fun setupRecyclerView() {
        val token = getTokenFromSharedPref()
        if (token.isEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_LONG).show()
            return
        }

        val bearerToken = "Bearer $token"
        Log.d("TOKEN_DEBUG", "Token dari SharedPreferences: $bearerToken")

        val apiService = ApiConfig.getRetrofitInstance().create(ApiService::class.java)
        val call = apiService.getRiwayat(bearerToken)

        call.enqueue(object : Callback<PesananKostumResponse> {
            override fun onResponse(
                call: Call<PesananKostumResponse>,
                response: Response<PesananKostumResponse>
            ) {
                if (response.isSuccessful) {
                    val riwayatList = response.body()?.data ?: emptyList()
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@RiwayatActyvity)
                    binding.recyclerView.adapter = RiwayatAdapter(riwayatList)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        this@RiwayatActyvity,
                        "Gagal: ${response.code()} - ${response.message()}\n$errorBody",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<PesananKostumResponse>, t: Throwable) {
                Toast.makeText(this@RiwayatActyvity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTokenFromSharedPref(): String {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE) // Pastikan sama seperti di login
        val token = sharedPreferences.getString("token", "")
        Log.d("TOKEN_DEBUG", "Token dari SharedPreferences: $token")
        return token ?: ""
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_beranda -> {
                    startActivity(Intent(this, BerandaActivity::class.java))
                    finish()
                    true
                }
                R.id.bottom_pesan -> {
                    startActivity(Intent(this, PesanActivity::class.java))
                    finish()
                    true
                }
                R.id.bottom_profil -> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
