package com.example.rants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityEditprofilBinding

class EditprofilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofilBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
