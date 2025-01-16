package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityEditprofilBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditprofilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(0, 0)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
