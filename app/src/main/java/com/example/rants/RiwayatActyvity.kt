package com.example.rants

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rants.adapter.RiwayatAdapter
import com.example.rants.databinding.RiwayatActyvityBinding
import com.example.rants.model.ItemRiwayat
import com.google.android.material.bottomnavigation.BottomNavigationView

class RiwayatActyvity : AppCompatActivity() {
    private lateinit var binding: RiwayatActyvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0) // Transisi layar
        binding = RiwayatActyvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur item yang dipilih di BottomNavigation
        binding.bottomNavigation.selectedItemId = R.id.bottom_riwayat

        // Siapkan data RecyclerView
        setupRecyclerView()

        // Atur navigasi bawah
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        // Data untuk RecyclerView
        val dataList = listOf(
            ItemRiwayat(R.drawable.riwayat, "Produk A", "Rp10.000", "Total 1 produk"),
            ItemRiwayat(R.drawable.riwayat, "Produk B", "Rp20.000", "Total 2 produk"),
            ItemRiwayat(R.drawable.riwayat, "Produk C", "Rp30.000", "Total 3 produk"),
            ItemRiwayat(R.drawable.riwayat, "Produk D", "Rp40.000", "Total 4 produk")
        )

        // Atur RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = RiwayatAdapter(dataList)
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigation

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