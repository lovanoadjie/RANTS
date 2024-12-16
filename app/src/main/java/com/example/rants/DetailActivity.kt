package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityDetailBinding
import com.example.rants.model.ProductDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan productId dari Intent
        val productId = intent?.getIntExtra("product_id", -1) ?: -1
        Log.d("DetailActivity", "Received Product ID: $productId")

        if (productId == -1) {
            Log.e("DetailActivity", "Product ID tidak diterima dengan benar!")
            finish() // Menghentikan activity jika productId tidak valid
        } else {
            Log.d("DetailActivity", "Received Product ID: $productId")
        }

        // Setup Toolbar
        setSupportActionBar(binding.toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Mengambil detail produk berdasarkan productId
        getDetailFromApi(productId)
    }

    private fun getDetailFromApi(productId: Int) {
        val apiService = ApiConfig.getProducts().create(ApiService::class.java)
        apiService.getProductById(productId).enqueue(object : Callback<ProductDetailResponse> {
            override fun onResponse(call: Call<ProductDetailResponse>, response: Response<ProductDetailResponse>) {
                if (response.isSuccessful) {
                    val product = response.body()?.data
                    if (product != null) {
                        // Menampilkan data produk
                        Log.d("DetailActivity", "Product details: ${product.nama_kostum}")
                        binding.namaKostum.text = product.nama_kostum
                        binding.jumlah.text = product.jumlah.toString()
                        binding.warna.text = product.warna
                        binding.ukuran.text = product.ukuran
                        binding.harga.text = product.harga.toString()

                        // Menampilkan gambar produk dengan Glide
                        Glide.with(this@DetailActivity)
                            .load(product.image)
                            .into(binding.image)
                    }
                } else {
                    Log.e("DetailActivity", "Response error: ${response.message()}")
                    Toast.makeText(this@DetailActivity, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("DetailActivity", "API call failed: ${t.message}")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()  // Kembali ke activity sebelumnya
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
