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
        val apiService = ApiConfig.getProducts().create(ApiService::class.java)
        apiService.getProducts().enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.data
                    if (products != null) {
                        // Simpan ke adapter dan set onItemClick
                        productAdapter = ProductAdapter(products).apply {
                            onItemClick = { product ->
                                // Pastikan product ID dikirim ke DetailActivity
                                val intent = Intent(this@KostumActivity, DetailActivity::class.java)
                                intent.putExtra("product_id", product.id)  // Mengirimkan ID produk
                                startActivity(intent)
                            }
                        }
                        binding.recyclerView.adapter = productAdapter
                    }
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@KostumActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


//    private fun onProductClick(product: kosta) {
//        // Handle click item and navigate to DetailActivity
//        val intent = Intent(this, DetailActivity::class.java)
//            intent.putExtra("product_id", product.id)  // Kirim id produk ke DetailActivity
//
//    }

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



