package com.example.rants

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityChatadminBinding
import androidx.recyclerview.widget.LinearLayoutManager

class ChatadminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatadminBinding
    private val messages = mutableListOf<Message>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatadminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fungsi untuk mengonversi dp ke px
        fun dpToPx(dp: Int): Int {
            val density = resources.displayMetrics.density
            return (dp * density).toInt()
        }

        // Menggunakan fungsi untuk jarak antar item
        binding.recyclerView.addItemDecoration(SpacingItemDecoration(dpToPx(4))) // 8dp jarak antar item

        // Setup RecyclerView
        adapter = ChatAdapter(messages)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Tombol kembali
        binding.backBtn.setOnClickListener { finish() }

        // Kirim pesan
        binding.messegeSendBtn.setOnClickListener {
            val messageText = binding.edittext.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.edittext.text.clear()
            }
        }
    }

    private fun sendMessage(messageText: String) {
        // Tambahkan pesan pengguna
        messages.add(Message(messageText, true))
        adapter.notifyItemInserted(messages.size - 1)
        binding.recyclerView.scrollToPosition(messages.size - 1)

        // Log untuk debugging
        Log.d("ChatadminActivity", "Message sent: $messageText")

        // Simulasi balasan
        simulateReply()
    }

    private fun simulateReply() {
        val reply = "Ini adalah balasan otomatis." // Balasan yang ingin Anda kirimkan
        messages.add(Message(reply, false)) // false untuk menunjukkan bahwa ini balasan
        adapter.notifyItemInserted(messages.size - 1)
        binding.recyclerView.scrollToPosition(messages.size - 1)

        // Log untuk debugging
        Log.d("ChatadminActivity", "Reply sent: $reply")
    }
}
