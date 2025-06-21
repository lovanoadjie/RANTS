package com.example.rants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityPesananTariBinding
import com.example.rants.model.TariDetailResponse

import com.example.rants.PembayaranActivity
import com.example.rants.model.PaymentRequest
import com.example.rants.model.PaymentResponse
import com.example.rants.model.PesananRequest
import com.example.rants.model.PesananResponse
import com.example.rants.model.PesananTariData
import com.example.rants.model.PesananTariRequest
import com.example.rants.model.PesananTariResponse
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PesananTariActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananTariBinding
    private var basePrice: Int = 0  // Ganti tipe data menjadi Double
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananTariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengambil tari_id dari Intent
        val tariId = intent?.getIntExtra("tari_id", -1) ?: -1
        Log.d("PesananTariActivity", "Received Tari ID: $tariId")

        if (tariId == -1) {
            Toast.makeText(this, "Tari ID tidak valid!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            getTariDetail(tariId)
        }

        // Setup tombol decrease
        binding.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updatePrice()
            }
        }

        // Setup tombol increase
        binding.btnIncrease.setOnClickListener {
            quantity++
            updatePrice()
        }

        // Setup tombol bayar sekarang
        binding.btnPayNow.setOnClickListener {
            createOrder(tariId)
        }

        // Date Picker
        binding.etDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
                binding.etDate.setText(selectedDate)
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        binding.etTime.setOnTimeChangedListener { _, hourOfDay, minute ->
            val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
            binding.etTime.contentDescription = selectedTime
        }

    }

    private fun getTariDetail(tariId: Int) {
        val apiService = ApiConfig.getTariDetails().create(ApiService::class.java)
        apiService.getTariById(tariId).enqueue(object : Callback<TariDetailResponse> {
            override fun onResponse(call: Call<TariDetailResponse>, response: Response<TariDetailResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("API_RESPONSE", response.body().toString())
                    val tari = response.body()?.data
                    if (tari != null) {
                        binding.jenistarian.text = tari.jenis_tarian ?: "Tarian tidak tersedia"
                        binding.harga.text =
                            "Rp ${formatCurrency(tari.harga ?: 0)}" // Format harga Double

                        Glide.with(this@PesananTariActivity)
                            .load(ApiConfig.getImageUrl() + tari.image)
                            .into(binding.image)

                        basePrice = tari.harga ?: 0  // Pastikan harga menggunakan tipe Double
                        updatePrice()
                    } else {
                        Toast.makeText(
                            this@PesananTariActivity,
                            "Produk tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                    Toast.makeText(
                        this@PesananTariActivity,
                        "Gagal mendapatkan detail produk",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<TariDetailResponse>, t: Throwable) {
                Toast.makeText(this@PesananTariActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun createPaymentTransaction(amount: Int) {
        // Ambil token dari SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token == null) {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Membuat request pembayaran
        val paymentRequest = PaymentRequest(amount.toDouble())  // Amount as Double

        val apiService = ApiConfig.getRetrofitInstance().create(ApiService::class.java)

        // Menambahkan Authorization header dengan Bearer token
        apiService.createTransaction("Bearer $token", paymentRequest)
            .enqueue(object : Callback<PaymentResponse> {
                override fun onResponse(
                    call: Call<PaymentResponse>,
                    response: Response<PaymentResponse>
                ) {
                    if (response.isSuccessful) {
                        val paymentResponse = response.body()
                        if (paymentResponse != null) {
                            // Handle response, e.g., show success dialog
                            val snapToken = paymentResponse.snap_token
                            val orderId = paymentResponse.order_id
                            // Proceed to show the payment confirmation dialog
                            showPaymentSuccessDialog(snapToken, orderId)
                        }
                    } else {
                        Toast.makeText(
                            this@PesananTariActivity,
                            "Gagal membuat transaksi pembayaran",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                    Toast.makeText(
                        this@PesananTariActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun convertDateFormat(dateStr: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateStr)
        return if (date != null) outputFormat.format(date) else ""
    }

    private fun updatePrice() {
        binding.tvQuantity.text = quantity.toString()
        binding.tvPrice.text =
            "Harga: Rp ${formatCurrency(basePrice * quantity)}"  // Update harga dengan quantity
    }

    private fun formatCurrency(value: Int): String {
        val numberFormat = NumberFormat.getInstance(Locale("id", "ID"))
        return numberFormat.format(value)
    }

    fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }


    private fun createOrder(tariId: Int) {
        val formattedDate = convertDateFormat(binding.etDate.text.toString())
        if (formattedDate.isEmpty()) {
            Toast.makeText(this, "Tanggal tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil jam dari TimePicker dengan benar
        val hour = binding.etTime.hour
        val minute = binding.etTime.minute
        val jamPemakaian = String.format("%02d:%02d", hour, minute)

        // Tetap ambil user_id seperti source code lamamu
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", 1) // default 1 kalau belum login

        val orderRequest = PesananTariRequest(
            penyewaan_jasa_taris_id = tariId,
            Users_id = userId,
            tanggal = formattedDate,
            jam_pemakaian = jamPemakaian,  // JAM SUDAH BENAR
            alamat = binding.etLocation.text.toString(),
            latitude = null,
            longitude = null,
            total_harga = basePrice * quantity,
            status_pesanan = "selesai"
        )

        // Create the API service instance
        val apiService = ApiConfig.getRetrofitInstance().create(ApiService::class.java)
        apiService.createTariOrder(orderRequest).enqueue(object : Callback<PesananTariResponse> {
            override fun onResponse(
                call: Call<PesananTariResponse>,
                response: Response<PesananTariResponse>
            ) {
                if (response.isSuccessful) {
                    // If the order was successfully created
                    Toast.makeText(
                        this@PesananTariActivity,
                        "Pesanan berhasil dibuat",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Now, call the payment API after the order is created
                    createPaymentTransaction(basePrice * quantity)
                } else {
                    // If the order creation failed
                    Toast.makeText(
                        this@PesananTariActivity,
                        "Gagal membuat pesanan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PesananTariResponse>, t: Throwable) {
                // Handle failure case
                Toast.makeText(
                    this@PesananTariActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showPaymentSuccessDialog(snapToken: String, orderId: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Pembayaran Berhasil")
        dialogBuilder.setMessage("Transaksi berhasil dilakukan! \n\nOrder ID: $orderId\nSnap Token: $snapToken")

        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Lakukan verifikasi pembayaran setelah sukses
            finish()
        }

        dialogBuilder.setCancelable(false)
        val dialog = dialogBuilder.create()
        dialog.show()
    }

}