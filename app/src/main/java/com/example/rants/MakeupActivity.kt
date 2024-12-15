package com.example.rants

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rants.adapter.MakeupAdapter
import com.example.rants.databinding.ActivityKostumBinding
import com.example.rants.adapter.ProductAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityMakeupBinding
import com.example.rants.model.Makeup
import com.example.rants.model.MakeupResponse
import com.example.rants.model.ProductResponse
import com.example.rants.model.kosta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMakeupBinding
    private lateinit var MakeupAdapter: MakeupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Panggil fungsi untuk mengambil data produk
        getMakeupFromApi()
    }

    private fun getMakeupFromApi() {
        Log.d("API Response", "Fetching products...")

        val apiService = ApiConfig.getMakeup().create(ApiService::class.java)
        apiService.getMakeup().enqueue(object : Callback<MakeupResponse> {
            override fun onResponse(call: Call<MakeupResponse>, response: Response<MakeupResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.data.isNotEmpty()) {
                        // Now use responseBody.data to populate the RecyclerView
                        MakeupAdapter = MakeupAdapter(responseBody.data)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@MakeupActivity)
                        binding.recyclerView.adapter = MakeupAdapter
                    } else {
                        Log.d("API Response", "No products found")
                        Toast.makeText(this@MakeupActivity, "Tidak ada produk ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "Error: ${response.message()}")
                    Toast.makeText(this@MakeupActivity, "Gagal memuat produk: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }



            override fun onFailure(call: Call<MakeupResponse>, t: Throwable) {
                Log.e("Kostum Activity", "Error: ${t.message}")
                t.printStackTrace() // Mencetak stack trace untuk debugging lebih lanjut
                Toast.makeText(this@MakeupActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
