package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityProfilBinding
import com.example.rants.model.LogoutResponse
import com.example.rants.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol untuk pergi ke Edit Profil
        binding.ubahprofilButton.setOnClickListener {
            goToEditprofilActivity()
        }

        // Bottom navigation setup
        binding.bottomNavigation.selectedItemId = R.id.bottom_profil
        setupBottomNavigation()
        overridePendingTransition(0, 0)

        // Tombol close (tambahkan aksi sesuai kebutuhan)
        binding.close.setOnClickListener {
            logout() // Tutup activity Profil
        }

        // Ambil token dari SharedPreferences dan panggil API untuk mengambil profil
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        Log.d("token", token.toString())

        if (token != null) {
            fetchUserProfile(token)
        } else {
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
            finish() // Jika token tidak ada, keluar dari activity
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            fetchUserProfile(token) // Panggil ulang API untuk mengambil profil setiap kali aktivitas kembali aktif
        } else {
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
            finish() // Jika token tidak ada, keluar dari activity
        }
    }

    private fun fetchUserProfile(token: String) {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        Log.d("ProfilActivity", "Fetching user profile with token: $token")

        apiService.getUserProfile("Bearer $token").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("ProfilActivity", "Response code: ${response.code()}")
                Log.d("ProfilActivity", "Response message: ${response.message()}")
                try {
                    val responseBody = response.body().toString()
                    Log.d("ProfilActivity", "Response body: $responseBody")
                } catch (e: Exception) {
                    Log.e("ProfilActivity", "Error logging response body: ${e.message}")
                }

                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null) {
                        val userProfile = userResponse.data
                        binding.nameEditText.setText(userProfile.name)
                        binding.emailEditText.setText(userProfile.email)
                        binding.phoneEditText.setText(userProfile.nohp)
                        // Menampilkan URL gambar profil (contoh menggunakan Glide)
                        Glide.with(this@ProfilActivity)
                            .load(userProfile.image_url)
                            .into(binding.fotoProfil)
                    } else {
                        Log.e("ProfilActivity", "User data is null")
                        Toast.makeText(this@ProfilActivity, "Error: User data is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("ProfilActivity", "Response error: ${response.message()}")
                    Toast.makeText(this@ProfilActivity, "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("ProfilActivity", "Failure: ${t.message}")
                Toast.makeText(this@ProfilActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        Log.d("Logout", "Token saat ini: $token")

        if (token != null) {
            val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
            apiService.logout("Bearer $token").enqueue(object : Callback<LogoutResponse> {
                override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                    if (response.isSuccessful) {
                        val logoutResponse = response.body()
                        if (logoutResponse != null) {
                            if (logoutResponse.status == "success") {
                                // Hapus token dari SharedPreferences
                                val editor = sharedPreferences.edit()
                                editor.remove("token")
                                editor.apply()

                                // Verifikasi penghapusan
                                val removedToken = sharedPreferences.getString("token", null)
                                Log.d("Logout", "Token setelah dihapus: $removedToken")

                                // Tampilkan pesan sukses dan kembali ke halaman login
                                Toast.makeText(this@ProfilActivity, logoutResponse.message, Toast.LENGTH_SHORT).show()
                                goToLoginActivity()
                            } else {
                                Toast.makeText(this@ProfilActivity, "Logout gagal", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@ProfilActivity, "Response body kosong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProfilActivity, "Gagal logout", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    Toast.makeText(this@ProfilActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Token tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
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
                R.id.bottom_riwayat -> {
                    startActivity(Intent(this, RiwayatActyvity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun goToEditprofilActivity() {
        val intent = Intent(this, EditprofilActivity::class.java)
        startActivity(intent)
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}