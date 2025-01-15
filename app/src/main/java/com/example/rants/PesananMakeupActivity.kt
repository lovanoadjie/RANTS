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
import com.example.rants.model.MakeupDetailResponse
import com.example.rants.model.PesananRequest
import com.example.rants.databinding.ActivityPesananMakeupBinding
import com.example.rants.model.PaymentRequest
import com.example.rants.model.PaymentResponse
import com.example.rants.model.PaymentVerificationRequest
import com.example.rants.model.PaymentVerificationResponse
import com.example.rants.model.PesananResponse
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class PesananMakeupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananMakeupBinding
    private var basePrice = 0
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananMakeupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val makeupId = intent?.getIntExtra("makeup_id", -1) ?: -1
        if (makeupId == -1) {
            Toast.makeText(this, "Makeup ID tidak valid!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            getMakeupDetail(makeupId)
        }

        binding.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updatePrice()
            }
        }

        binding.btnIncrease.setOnClickListener {
            quantity++
            updatePrice()
        }

        binding.btnPayNow.setOnClickListener {
            if (binding.etLocation.text.isNullOrEmpty()) {
                Toast.makeText(this, "Silakan masukkan lokasi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createOrder(makeupId)
        }

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
    }

    private fun getMakeupDetail(makeupId: Int) {
        val apiService = ApiConfig.getRetrofitInstance().create(ApiService::class.java)
        apiService.getMakeupById(makeupId).enqueue(object : Callback<MakeupDetailResponse> {
            override fun onResponse(
                call: Call<MakeupDetailResponse>,
                response: Response<MakeupDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val makeup = response.body()?.data
                    if (makeup != null) {
                        val category =
                            makeup.kategory?.getCategoryName() ?: "Kategori Tidak Diketahui"
                        binding.kategory.text = category
                        binding.harga.text = "Rp ${formatCurrency(makeup.harga ?: 0)}"
                        val imageUrl = makeup.image?.let { ApiConfig.getImageUrl() + it } ?: ""
                        Glide.with(this@PesananMakeupActivity)
                            .load(imageUrl)
                            .into(binding.image)
                        basePrice = makeup.harga ?: 0
                        updatePrice()
                    } else {
                        Toast.makeText(
                            this@PesananMakeupActivity,
                            "Makeup tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@PesananMakeupActivity,
                        "Gagal mendapatkan detail makeup",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MakeupDetailResponse>, t: Throwable) {
                Toast.makeText(
                    this@PesananMakeupActivity,
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
        binding.tvPrice.text = "Harga: Rp ${formatCurrency(basePrice * quantity)}"
    }

    private fun createOrder(makeupId: Int) {
        val formattedDate = convertDateFormat(binding.etDate.text.toString())
        if (formattedDate.isEmpty()) {
            Toast.makeText(this, "Tanggal tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val orderRequest = PesananRequest(
            make_ups_id = makeupId,
            Users_id = 1, // Replace with actual user ID
            tanggal_pesanan = formattedDate,
            lokasi_pemesanan = binding.etLocation.text.toString(),
            alamat = "Alamat", // You can modify or add actual address handling here
            latitude = null, // Update with real coordinates if needed
            longitude = null, // Update with real coordinates if needed
            total_harga = basePrice * quantity,
            status_pesanan = "selesai"
        )

        val apiService = ApiConfig.getRetrofitInstance().create(ApiService::class.java)
        apiService.createOrder(orderRequest).enqueue(object : Callback<PesananResponse> {
            override fun onResponse(
                call: Call<PesananResponse>,
                response: Response<PesananResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@PesananMakeupActivity,
                        "Pesanan berhasil dibuat",
                        Toast.LENGTH_SHORT
                    ).show()
                    createPaymentTransaction(basePrice * quantity)
                } else {
                    // Log informasi respons untuk debugging
                    Log.e("API_ERROR", "Response code: ${response.code()}")
                    Log.e("API_ERROR", "Response message: ${response.message()}")
                    Log.e("API_ERROR", "Response body: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@PesananMakeupActivity,
                        "Gagal membuat pesanan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PesananResponse>, t: Throwable) {
                Toast.makeText(
                    this@PesananMakeupActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
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
                            this@PesananMakeupActivity,
                            "Gagal membuat transaksi pembayaran",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                    Toast.makeText(
                        this@PesananMakeupActivity,
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

    private fun formatCurrency(value: Int): String {
        val numberFormat = NumberFormat.getInstance(Locale("id", "ID"))
        return numberFormat.format(value)
    }
}
