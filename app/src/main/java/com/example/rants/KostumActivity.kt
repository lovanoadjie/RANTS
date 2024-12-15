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
import com.example.rants.databinding.ActivityKostumBinding
import com.example.rants.adapter.ProductAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.model.ProductResponse
import com.example.rants.model.kosta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KostumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKostumBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKostumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Panggil fungsi untuk mengambil data produk
        getProductsFromApi()
    }

    private fun getProductsFromApi() {
        Log.d("API Response", "Fetching products...")

        val apiService = ApiConfig.getProducts().create(ApiService::class.java)
        apiService.getProducts().enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.data.isNotEmpty()) {
                        // Inisialisasi productAdapter setelah mendapatkan data dari API
                        productAdapter = ProductAdapter(responseBody.data)

                        // Mengatur RecyclerView dengan adapter yang sudah diinisialisasi
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@KostumActivity)
                        binding.recyclerView.adapter = productAdapter

                        // Menangani item click setelah adapter siap
                        productAdapter.onItemClick = { product ->
                            Log.d("KostumActivity", "Sending Product ID: ${product.id}") // Log ID yang akan dikirim
                            val intent = Intent(this@KostumActivity, DetailActivity::class.java)
                            intent.putExtra("product_id", product.id) // Pastikan ID tidak null atau 0
                            startActivity(intent)
                        }

                        responseBody.data.forEach { product ->
                            Log.d("API Response", "Product ID: ${product.id}, Name: ${product.nama_kostum}")
                        }
                    } else {
                        Log.d("API Response", "No products found")
                        Toast.makeText(this@KostumActivity, "Tidak ada produk ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "Error: ${response.message()}")
                    Toast.makeText(this@KostumActivity, "Gagal memuat produk: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("Kostum Activity", "Error: ${t.message}")
                t.printStackTrace()
                Toast.makeText(this@KostumActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


