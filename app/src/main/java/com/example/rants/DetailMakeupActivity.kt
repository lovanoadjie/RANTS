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
import com.example.rants.databinding.ActivityDetailMakeupBinding
import com.example.rants.model.Makeup
import com.example.rants.model.MakeupDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMakeupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMakeupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMakeupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.orderDetailButton.setOnClickListener {
            goToPesanan()
        }

        // Mendapatkan productId dari Intent
        val makeupId = intent?.getIntExtra("makeup_id", -1) ?: -1
        Log.d("DetailMakeupActivity", "Received Makeup ID: $makeupId")

        if (makeupId == -1) {
            Log.e("DetailMakeupActivity", "Makeup ID tidak diterima dengan benar!")
            finish() // Menghentikan activity jika makeupId tidak valid
        } else {
            Log.d("DetailMakeupActivity", "Received Makeup ID: $makeupId")
        }

        // Setup Toolbar
        setSupportActionBar(binding.toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Mengambil detail produk berdasarkan productId
        getDetailMakeupFromApi(makeupId)
    }

    private fun getDetailMakeupFromApi(makeupId: Int) {
        val apiService = ApiConfig.getMakeup().create(ApiService::class.java)
        apiService.getMakeupById(makeupId).enqueue(object : Callback<MakeupDetailResponse> {
            override fun onResponse(call: Call<MakeupDetailResponse>, response: Response<MakeupDetailResponse>) {
                if (response.isSuccessful) {
                    val makeup = response.body()?.data
                    if (makeup != null) {
                        // Menampilkan data produk
                        Log.d("DetailMakeupActivity", "makeup details: ${makeup.Kategory}")
                        val category = makeup.Kategory?.name ?: "Unknown Category"
                        binding.Kategory.text = category
                        binding.harga.text = makeup.harga.toString()

                        // URL Gambar
                        val imageUrl = ApiConfig.getImageUrl() + makeup.image
                        Log.d("Hello", "coba image: $imageUrl")

                        // Menampilkan gambar produk dengan Glide
                        Glide.with(this@DetailMakeupActivity)
                            .load(imageUrl)
                            .into(binding.image)
                    }
                } else {
                    Log.e("DetailMakeupActivity", "Response error: ${response.message()}")
                    Toast.makeText(this@DetailMakeupActivity, "Gagal mengambil data makeup", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MakeupDetailResponse>, t: Throwable) {
                Toast.makeText(this@DetailMakeupActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("DetailMakeupActivity", "API call failed: ${t.message}")
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

    private fun goToPesanan() {
        val intent = Intent(this, PesananActivity::class.java).also {
            startActivity(it)
        }
    }
}
