package com.example.rants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rants.databinding.ActivityPesanBinding

class PesanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust the padding for the view (binding.ac) to account for system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.ac) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
