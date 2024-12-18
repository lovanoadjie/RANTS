package com.example.rants

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PemesananTariActivity : AppCompatActivity() {

    private var quantity = 1 // Default jumlah item
    private val pricePerItem = 50000 // Harga per item
    private lateinit var tvQuantity: TextView
    private lateinit var tvPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemesanan_tari)

        // Inisialisasi View
        val etDate = findViewById<EditText>(R.id.et_date)
        val etTime = findViewById<EditText>(R.id.et_time)
        val etLocation = findViewById<EditText>(R.id.et_location)
        val btnDecrease = findViewById<Button>(R.id.btn_decrease)
        val btnIncrease = findViewById<Button>(R.id.btn_increase)
        val btnOk = findViewById<Button>(R.id.btn_ok)
        val btnPayNow = findViewById<Button>(R.id.btn_pay_now)
        tvQuantity = findViewById(R.id.tv_quantity)
        tvPrice = findViewById(R.id.tv_price)

        // Update harga saat Activity dibuat
        updatePrice()

        // Tombol Tambah Jumlah
        btnIncrease.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
            updatePrice()
        }

        // Tombol Kurangi Jumlah
        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
                updatePrice()
            } else {
                Toast.makeText(this, "Jumlah tidak bisa kurang dari 1", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol OK: Validasi input form
        btnOk.setOnClickListener {
            val date = etDate.text.toString().trim()
            val time = etTime.text.toString().trim()
            val location = etLocation.text.toString().trim()

            if (date.isEmpty() || time.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Bayar Sekarang
        btnPayNow.setOnClickListener {
            val totalPayment = pricePerItem * quantity
            Toast.makeText(this, "Total Pembayaran: Rp $totalPayment", Toast.LENGTH_LONG).show()
        }
    }

    // Fungsi untuk memperbarui harga berdasarkan jumlah item
    private fun updatePrice() {
        val totalPrice = pricePerItem * quantity
        tvPrice.text = "Harga: Rp $totalPrice"
    }
}
