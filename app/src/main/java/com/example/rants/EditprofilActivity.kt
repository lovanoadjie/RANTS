package com.example.rants

import android.content.Intent
import android.os.Bundle
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

    }




}
