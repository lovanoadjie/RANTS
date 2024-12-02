package com.example.rants

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rants.databinding.ActivityBerandaBinding
import com.example.rants.databinding.RiwayatActyvityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class RiwayatActyvity : AppCompatActivity() {
    private lateinit var binding: RiwayatActyvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RiwayatActyvityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.selectedItemId = R.id.bottom_riwayat

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
                R.id.bottom_profil -> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    finish()
                    true
                }

                else -> false
            }
        }
    }

}