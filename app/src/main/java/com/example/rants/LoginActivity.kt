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
import com.example.rants.model.LoginRequest
import com.example.rants.model.AuthResponse
import com.example.rants.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle klik tombol login
        binding.loginButton.setOnClickListener {
            val email = binding.editText1.text.toString().trim()
            val password = binding.editText2.text.toString().trim()

            Log.d("LoginActivity", "Data yang dikirimkan: Email: $email, Password: $password")

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil fungsi login
            loginUser(email, password)
        }

        //handle login google
        binding.loginGoogle.setOnClickListener{
            Toast.makeText(this, "Tombol Login Google", Toast.LENGTH_SHORT).show()
        }

        // Handle klik tombol daftar
        binding.daftarTextView.setOnClickListener {
            goToRegisterActivity()
        }

        // Menampilkan atau menyembunyikan password
        handleShowPassword()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
    }

    private fun handleShowPassword() {
        val passwordEditText = binding.editText2
        val showPasswordCheckbox = binding.showPasswordCheckbox

        showPasswordCheckbox.setOnCheckedChangeListener { _, isChecked ->
            passwordEditText.inputType = if (isChecked) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Mengatur posisi kursor tetap di akhir teks
            passwordEditText.setSelection(passwordEditText.text.length)
        }
    }

    private fun loginUser(email: String, password: String) {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        Log.d("LoginActivity", "Memulai permintaan API untuk login")
        apiService.login(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LoginActivity", "Respons API diterima: ${response.code()}")

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        saveTokenToSharedPreferences(authResponse.token)
                        Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                        goToBerandaActivity()
                    } else {
                        Log.e("LoginActivity", "Token tidak ditemukan di respons")
                        Toast.makeText(this@LoginActivity, "Login gagal: Token tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginActivity", "Login gagal: ${response.message()} (Code: ${response.code()})")
                    Log.e("LoginActivity", "Error Body: $errorBody")
                    Toast.makeText(this@LoginActivity, "Login gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("LoginActivity", "Gagal menghubungi server: ${t.message}")
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("token", token).apply()

        Log.d("LoginActivity", "Token disimpan di SharedPreferences: $token")
    }

    private fun goToBerandaActivity() {
        Log.d("LoginActivity", "Navigasi ke BerandaActivity")
        val intent = Intent(this, BerandaActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToRegisterActivity() {
        Log.d("LoginActivity", "Navigasi ke RegisterActivity")
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
