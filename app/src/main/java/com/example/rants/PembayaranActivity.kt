package com.example.rants

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.R
import com.example.rants.databinding.ActivityDetailTariBinding
import com.example.rants.databinding.ActivityPembayaranBinding

class PembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPembayaranBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Ambil referensi TextView untuk nomor rekening
        val accountNumberTextView = findViewById<TextView>(R.id.accountNumber)

        // Ambil referensi tombol salin
        val copyButton = findViewById<ImageButton>(R.id.copyButton)

        // Tambahkan aksi klik untuk tombol salin
        copyButton.setOnClickListener {
            val accountNumber = accountNumberTextView.text.toString()
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Nomor Rekening", accountNumber)
            clipboard.setPrimaryClip(clip)

            // Tampilkan pesan berhasil
            Toast.makeText(this, "Nomor rekening berhasil disalin!", Toast.LENGTH_SHORT).show()
        }
    }
}
