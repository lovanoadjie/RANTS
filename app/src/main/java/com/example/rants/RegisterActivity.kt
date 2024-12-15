package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityRegisterBinding
import com.example.rants.model.AuthResponse
import com.example.rants.model.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle tombol login
        binding.loginButton.setOnClickListener {
            goToLoginActivity()
        }

        // Handle tombol daftar
        binding.setdaftarButton.setOnClickListener {
            val name = binding.editText1.text.toString().trim()
            val email = binding.editText2.text.toString().trim()
            val nohp = binding.editTextNoHP.text.toString().trim()
            val password = binding.editText3.text.toString().trim()

            // Validasi input
            if (name.isEmpty() || email.isEmpty() || nohp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil fungsi registrasi
            registerUser(name, email, nohp, password)
        }
    }

    private fun registerUser(name: String, email: String, nohp: String, password: String) {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        val registerRequest = RegisterRequest(
            name = name,
            email = email,
            nohp = nohp,
            password = password,
            password_confirmation = password // Password harus sesuai untuk validasi backend
        )

        // Panggil API untuk registrasi
        apiService.register(registerRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registrasi berhasil! Silakan login.",
                            Toast.LENGTH_SHORT
                        ).show()
                        goToLoginActivity()
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registrasi gagal: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("RegisterActivity", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Gagal terhubung ke server: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("RegisterActivity", "Failure: ${t.message}")
            }
        })
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
