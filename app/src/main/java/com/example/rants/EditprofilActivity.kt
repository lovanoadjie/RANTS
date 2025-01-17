package com.example.rants

import android.R
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
import com.example.rants.model.UserResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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
        } else if (token != null) {
            val userProfileMap = mapOf(
                "name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
                "email" to email.toRequestBody("text/plain".toMediaTypeOrNull()),
                "nohp" to phone.toRequestBody("text/plain".toMediaTypeOrNull())
            )

            val imageUri = binding.fotoProfil.tag as? Uri
            val imagePart = imageUri?.let { uri ->
                val file = File(uri.path ?: "")
                val imageRequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("image", file.name, imageRequestBody)
            }

            val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
            apiService.updateUserProfile("Bearer $token", userProfileMap, imagePart).enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditprofilActivity, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EditprofilActivity, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@EditprofilActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                // Menangani aksi tombol back
                onBackPressed()  // Fungsi ini akan membawa pengguna kembali ke halaman sebelumnya
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
