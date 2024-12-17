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
import com.example.rants.databinding.ActivityDetailTariBinding
import com.example.rants.model.TariDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTariActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTariBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTariBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.orderButton1.setOnClickListener(){
            goToPesan()
        }

        val tariId = intent?.getIntExtra("tari_id", -1) ?: -1
        Log.d("DetailTariActivity", "Received Tari ID: $tariId")

        if (tariId == -1) {
            Log.e("DetailActivity", "Tari ID tidak diterima dengan benar!")
            finish() // Menghentikan activity jika tariId tidak valid
        } else {
            Log.d("DetailActivity", "Received Tari ID: $tariId")
        }

        // Setup Toolbar
        setSupportActionBar(binding.toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Mengambil detail produk berdasarkan tariId
        getDetailTariFromApi(tariId)


    }

    private fun getDetailTariFromApi(tariId: Int) {
        val apiService = ApiConfig.getTari().create(ApiService::class.java)
        apiService.getTariById(tariId).enqueue(object : Callback<TariDetailResponse> {
            override fun onResponse(call: Call<TariDetailResponse>, response: Response<TariDetailResponse>) {
                if (response.isSuccessful) {
                    val tari = response.body()?.data
                    if (tari != null) {
                        // Menampilkan data produk
                        Log.d("DetailActivityTari", "Tari details: ${tari.jenis_tarian}")
                        binding.namaTari.text = tari.jenis_tarian
                        binding.jumlahPenari.text = tari.jumlah_penari.toString()
                        binding.deskripsi.text = tari.deskripsi_acara
                        binding.harga.text = tari.harga.toString()
                        val baseUrl =  ApiConfig.getImageUrl()
                        val imageUrl = baseUrl + tari.image

                        Log.d("Hello", "coba image: $imageUrl")
                        // Menampilkan gambar produk dengan Glide
                        Glide.with(this@DetailTariActivity)
                            .load(imageUrl)
                            .into(binding.image)
                    }
                } else {
                    Log.e("DetailTariActivity", "Response error: ${response.message()}")
                    Toast.makeText(this@DetailTariActivity, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TariDetailResponse>, t: Throwable) {
                Toast.makeText(this@DetailTariActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("DetailTariActivity", "API call failed: ${t.message}")
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

    private fun goToPesan() {
        val intent = Intent(this, PesananActivity::class.java).also {
            startActivity(it)    }
    }
}