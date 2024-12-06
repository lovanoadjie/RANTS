package com.example.rants

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.rants.databinding.ActivityKostumBinding

class KostumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKostumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKostumBinding.inflate(layoutInflater)

        binding.pesankostumButton.setOnClickListener{
            goToWhaynotcarouselActivity()
        }
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }
    private fun goToWhaynotcarouselActivity() {
        val intent = Intent(this, WhaynotcarouselActivity::class.java)
        startActivity(intent)
    }
}


