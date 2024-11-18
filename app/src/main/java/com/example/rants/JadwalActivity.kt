package com.example.rants

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rants.databinding.ActivityJadwalBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class JadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.sheet)

        // Apply settings to the BottomSheetBehavior
        bottomSheetBehavior.apply {
            peekHeight = 700   // Set peek height
            state = BottomSheetBehavior.STATE_COLLAPSED  // Set initial state
        }
    }
}