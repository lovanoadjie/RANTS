package com.example.rants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityChatadminBinding

class ChatadminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatadminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menggunakan View Binding
        binding = ActivityChatadminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Contoh penggunaan binding untuk mengakses View
        binding.backBtn.setOnClickListener {
            finish() // Mengakhiri aktivitas
        }

        binding.messegeSendBtn.setOnClickListener {
            val message = binding.edittext.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.edittext.text.clear() // Membersihkan EditText
            }
        }
    }

    private fun sendMessage(message: String) {

    }
}
