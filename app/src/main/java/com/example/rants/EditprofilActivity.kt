package com.example.rants

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityEditprofilBinding
import com.example.rants.model.UserProfile
import com.example.rants.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditprofilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.saveButton.setOnClickListener {
            saveProfile()
        }

        overridePendingTransition(0, 0)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            fetchUserProfile(token)
        } else {
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchUserProfile(token: String) {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        apiService.getUserProfile("Bearer $token").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { userProfile ->
                        binding.nameEditText.setText(userProfile.name)
                        binding.emailEditText.setText(userProfile.email)
                        binding.phoneEditText.setText(userProfile.nohp)
                        Glide.with(this@EditprofilActivity)
                            .load(userProfile.image_url)
                            .into(binding.fotoProfil)
                    } ?: Toast.makeText(this@EditprofilActivity, "Error: Data user tidak ditemukan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@EditprofilActivity, "Gagal mengambil profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@EditprofilActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveProfile() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val phone = binding.phoneEditText.text.toString()

        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (name.isBlank() || email.isBlank() || phone.isBlank()) {
            binding.saveButton.error = "Semua kolom harus diisi"
            return
        }

        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat objek UserProfile
        val userProfile = UserProfile( // ID tidak perlu diubah
            name = name,
            email = email,
            nohp = phone,
            image_url = null // Gambar tidak perlu diubah
        )

        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        apiService.updateUserProfile("Bearer $token", userProfile).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditprofilActivity, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("EditprofilActivity", "Response error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@EditprofilActivity, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("EditprofilActivity", "Request failed", t)
                Toast.makeText(this@EditprofilActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            androidx.appcompat.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}