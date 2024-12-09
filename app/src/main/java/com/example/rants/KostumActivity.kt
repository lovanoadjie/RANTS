package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rants.databinding.ActivityKostumBinding
import com.example.rants.adapter.ProductAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.model.Product
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

        // Setup RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Panggil fungsi untuk mengambil data produk
        getProductsFromApi()
    }

    private fun getProductsFromApi() {
        Log.d("API Response", "Raw Response: pepk")

        val apiService = ApiConfig.getProducts().create(ApiService::class.java)
        apiService.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val rawResponse = response.errorBody()?.string() ?: "No error body"
                    Log.d("API Response", "Raw Response: $rawResponse")
                    Log.d("API Response", "Parsed Response: $responseBody")

                    if (responseBody != null && responseBody.isNotEmpty()) {
                        productAdapter = ProductAdapter(responseBody)
                        binding.recyclerView.adapter = productAdapter
                        Log.d("API Response", "Raw Response: kont")
                    } else {
                        Log.d("API Response", "Raw Response: jem")

                        Toast.makeText(this@KostumActivity, "Tidak ada produk ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("API Error", "Error: ${response.message()}")
                    Toast.makeText(this@KostumActivity, "Gagal memuat produk: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@KostumActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
