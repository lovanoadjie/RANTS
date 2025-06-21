package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityLoginBinding
import com.example.rants.model.AuthResponse
import com.example.rants.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Password visibility
        togglePasswordVisibility()
        binding.showPasswordIcon.setOnClickListener {
            togglePasswordVisibility()
        }

        // Tombol login
        binding.loginButton.setOnClickListener {
            val email = binding.editText1.text.toString().trim()
            val password = binding.editText2.text.toString().trim()

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        // Navigasi ke halaman register
        binding.daftarTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.editText2.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.showPasswordIcon.setImageResource(R.drawable.view)
        } else {
            binding.editText2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.showPasswordIcon.setImageResource(R.drawable.hide)
        }
        binding.editText2.setSelection(binding.editText2.text.length)
    }

    private fun loginUser(email: String, password: String) {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        apiService.login(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    val token = authResponse?.data?.token

                    if (!token.isNullOrEmpty()) {
                        saveTokenToSharedPreferences(token)
                        Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                        goToBerandaActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, "Token kosong!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(
                        this@LoginActivity,
                        "Login gagal: ${response.code()} - ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("Login", "Error: $error")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Login", "onFailure: ${t.message}")
            }
        })
    }

    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("token", token).apply()
        Log.d("LoginActivity", "Token disimpan: $token")
    }

    private fun goToBerandaActivity() {
        val intent = Intent(this, BerandaActivity::class.java)
        startActivity(intent)
        finish()
    }
}