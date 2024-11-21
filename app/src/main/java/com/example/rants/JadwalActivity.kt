package com.example.rants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // Gunakan Toolbar dari androidx
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

        // Gunakan androidx Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Aktifkan tombol kembali
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigasi kembali saat tombol diklik
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
