package com.example.rants

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityProfilBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding // Perbaiki nama binding agar sesuai dengan file XML layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater) // Pastikan nama ini sesuai dengan file binding
        setContentView(binding.root)
        binding.bottomNavigation.selectedItemId = R.id.bottom_profil
        setupBottomNavigation()
    }
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_beranda -> {
                    startActivity(Intent(this, BerandaActivity::class.java))
                    finish()
                    true
                }
                R.id.bottom_pesan -> {
                    startActivity(Intent(this, PesanActivity::class.java))
                    finish()
                    true
                }
                R.id.bottom_riwayat-> {
                    startActivity(Intent(this, RiwayatActyvity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }

}
