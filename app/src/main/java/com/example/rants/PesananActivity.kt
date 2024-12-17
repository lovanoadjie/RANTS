package com.example.rants

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityPesananBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class PesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater) // Menggunakan binding
        setContentView(binding.root)

        // Mengakses EditText dan AutoCompleteTextView melalui binding
        val etDate = binding.etDate
        val etLocation = binding.etLocation
        val btnDecrease = binding.btnDecrease
        val btnIncrease = binding.btnIncrease
        val tvQuantity = binding.tvQuantity
        val tvPrice = binding.tvPrice

        // Set default harga
        val basePrice = 10000
        tvPrice.text = "Harga: Rp $basePrice"

        // Mengatur jumlah default
        var quantity = 1

        // Mengatur event pada tombol decrease
        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
                tvPrice.text = "Harga: Rp ${basePrice * quantity}"
            }
        }

        // Mengatur event pada tombol increase
        btnIncrease.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
            tvPrice.text = "Harga: Rp ${basePrice * quantity}"
        }

        // Date Picker
        etDate.setOnClickListener {
            // Buat instance MaterialDatePicker dengan tema kustom
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal") // Judul dialog
                .setTheme(R.style.CustomMaterialDatePicker) // Tema yang dibuat di styles.xml
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default hari ini
                .build()

            // Event tombol OK
            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
                etDate.setText(selectedDate) // Memperbarui EditText
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }
    }
}