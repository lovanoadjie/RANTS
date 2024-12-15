package com.example.rants

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.rants.adapter.ImageAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityBerandaBinding
import com.example.rants.model.Gallery
import com.example.rants.model.GalleryResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class BerandaActivity : AppCompatActivity() {

    private lateinit var textViewDescription: TextView
    private lateinit var binding: ActivityBerandaBinding
    private lateinit var adapter: ImageAdapter
    private var listGambar = ArrayList<Gallery>()
    private lateinit var viewPager: ViewPager2
    private lateinit var dots: ArrayList<TextView>
    private val slideHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        overridePendingTransition(0, 0)

        textViewDescription = binding.textViewDescription

        // Inisialisasi viewPager
        viewPager = findViewById(R.id.view_pager)

        dots = ArrayList()
        // Panggil API untuk memuat data gambar
        getGalleriesFromApi()

        // Menggunakan adapter untuk ViewPager
        adapter = ImageAdapter(listGambar)
        viewPager.adapter = adapter

        binding.JadwalButton.setOnClickListener() {
            goToJadwalActivity()
        }

        binding.mesegeBtn.setOnClickListener() {
            goToChatadminActivity()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectDot(position)
                super.onPageSelected(position)

                // Update deskripsi gambar berdasarkan posisi yang dipilih
                val currentGallery = listGambar[position]
                binding.textViewDescription.text = currentGallery.description

                Log.d(
                    "BerandaActivity",
                    "TextView Description: ${binding.textViewDescription.text}"
                )


                slideHandler.removeCallbacks(sliderRun)
                slideHandler.postDelayed(sliderRun, 3000)
            }
        })
    }

    private val sliderRun = Runnable {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    private fun setIndicator() {
        // Pastikan dots sudah diinisialisasi
        for (i in 0 until listGambar.size) {
            dots.add(TextView(this))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                dots[i].text = Html.fromHtml("&#9679")
            }
            dots[i].textSize = 18f
            binding.dotsIndecator.addView(dots[i])
        }
    }

    private fun selectDot(position: Int) {
        for (i in 0 until listGambar.size) {
            if (i == position) {
                dots[i].setTextColor(
                    ContextCompat.getColor(
                        this,
                        com.google.android.material.R.color.design_default_color_primary
                    )
                )
            } else {
                dots[i].setTextColor(
                    ContextCompat.getColor(
                        this,
                        com.google.android.material.R.color.design_default_color_secondary
                    )
                )
            }
        }
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_pesan -> {
                    startActivity(Intent(this, PesanActivity::class.java))
                    finish()
                    true
                }

                R.id.bottom_riwayat -> {
                    startActivity(Intent(this, RiwayatActyvity::class.java))
                    finish()
                    true
                }

                R.id.bottom_profil -> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }

    // Fungsi untuk mengambil data gambar dari API
    private fun getGalleriesFromApi() {
        Log.d("ApiConfig", "ApiConfig getGalleries dipanggil!")

        val apiService = ApiConfig.getGalleries().create(ApiService::class.java)
        apiService.getGalleries().enqueue(object : Callback<GalleryResponse> {
            override fun onResponse(
                call: Call<GalleryResponse>,
                response: Response<GalleryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("ApiConfig", "API Response: ${responseBody?.data}")

                    // Check if data is not empty
                    if (responseBody != null && responseBody.data.isNotEmpty()) {
                        // Clear and add new data to the list
                        listGambar.clear()
                        listGambar.addAll(responseBody.data)

                        // Update adapter to display new data
                        adapter.notifyDataSetChanged()

                        // Ensure the indicator is updated after loading the images
                        setIndicator()
                    } else {
                        // If no data is found, show a message
                        Toast.makeText(
                            this@BerandaActivity,
                            "Tidak ada gambar ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // If the response failed, show an error message
                    Log.e("API Error", "HTTP Error: ${response.code()} - ${response.message()}")
                    Toast.makeText(
                        this@BerandaActivity,
                        "Gagal memuat gambar: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GalleryResponse>, t: Throwable) {
                // Handle connection failure
                Log.e("API Failure", "Error: ${t.message}", t)
                Toast.makeText(this@BerandaActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun goToJadwalActivity() {
        val intent = Intent(this, JadwalActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun goToChatadminActivity() {
        val url = "https://wa.me/6285263945612?text=Halo,%20saya%20ingin%20menghubungi%20Anda" // URL lengkap

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        try {
            startActivity(intent) // Langsung membuka chat WhatsApp
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal membuka WhatsApp. Pastikan WhatsApp terpasang.", Toast.LENGTH_SHORT).show()
        }
    }

}