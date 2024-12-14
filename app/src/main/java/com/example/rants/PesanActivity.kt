package com.example.rants

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rants.databinding.ActivityPesanBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class PesanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(0, 0)


        binding = ActivityPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.selectedItemId = R.id.bottom_pesan


        binding.sewapenariButton.setOnClickListener(){
            goToJasatariActivity()
        }
        binding.sewakostumButton1.setOnClickListener(){
            goToKostumActivity()
        }

        binding.sewamakeupButton2.setOnClickListener(){
            goToMakeupActivity()
        }
        setupBottomNavigation()
    }

    private fun goToMakeupActivity() {
        val intent = Intent(this, MakeupActivity::class.java).also {
            startActivity(it)    }
    }

    private fun goToJasatariActivity() {
        val intent = Intent(this, JasatariActivity::class.java).also {
            startActivity(it)
        }
    }
    private fun goToKostumActivity() {
        val intent = Intent(this, KostumActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_beranda-> {
                    startActivity(Intent(this, BerandaActivity::class.java))
                    finish()
                    true
                }
                R.id.bottom_riwayat-> {
                    startActivity(Intent(this, RiwayatActyvity::class.java))
                    finish()
                    true
                }
                R.id.bottom_profil-> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}