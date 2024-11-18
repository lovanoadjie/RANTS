package com.example.rants

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rants.databinding.ActivityBerandaBinding
import com.example.rants.databinding.ActivityLoginBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BerandaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBerandaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.sheet)

        // Apply settings to the BottomSheetBehavior
        bottomSheetBehavior.apply {
            peekHeight = 500  // Set peek height
            state = BottomSheetBehavior.STATE_COLLAPSED  // Set initial state
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.sheet) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemInsets.bottom + 200 // Tambahkan 50dp untuk menaikkan BottomSheet
            )
            insets
        }
}
}
