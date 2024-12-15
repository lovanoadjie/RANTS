package com.example.rants

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rants.adapter.ProductAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityDetailBinding
import com.example.rants.databinding.ActivityKostumBinding
//import com.example.rants.model.DetailResponse
import com.example.rants.model.ProductResponse
import com.example.rants.model.kosta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the productId from Intent, inside onCreate() to ensure the Intent is fully loaded
        val productId = intent?.getIntExtra("product_id", -1) ?: -1
        Log.d("DetailActivity", "Received Product ID: $productId") // Log productId inside onCreate()

        // Menampilkan ID produk untuk verifikasi
        Log.d("DetailActivity", "Product ID: $productId")

        // Setup Toolbar
        setSupportActionBar(binding.toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.orderButton.setOnClickListener(){
            goToPesanActivity()
        }



        getDetailFromApi(productId)  // Pass productId to the function
    }

    private fun goToPesanActivity() {
        val intent = Intent(this, PesananActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun getDetailFromApi(productId: Int) {
        Log.d("API Response", "Fetching product details...")

        val apiService = ApiConfig.getProducts().create(ApiService::class.java)
        apiService.getProducts(productId).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val productResponse = response.body()
                    val product = productResponse?.data?.find { it.id == productId }  // Menemukan produk yang sesuai
                    if (product != null) {
                        // Bind data ke UI
                        binding.namaKostum.text = product.nama_kostum
                        binding.jumlah.text = product.jumlah.toString()
                        binding.warna.text = product.warna
                        binding.ukuran.text = product.ukuran
                        binding.harga.text = product.harga.toString()
                    } else {
                        Toast.makeText(this@DetailActivity, "Product not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "Error: ${response.message()}")
                    Toast.makeText(this@DetailActivity, "Failed to load product details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("DetailActivity", "Error: ${t.message}")
                t.printStackTrace()
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                onBackPressed()  // Go back to the previous screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


