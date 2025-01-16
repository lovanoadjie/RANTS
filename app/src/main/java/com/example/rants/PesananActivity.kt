package com.example.rants

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityPesananBinding
import com.example.rants.model.PaymentRequest
import com.example.rants.model.PaymentResponse
import com.example.rants.model.PesananKostumRequest
import com.example.rants.model.PesananKostumResponse
import com.example.rants.model.ProductDetailResponse
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PesananActivity : AppCompatActivity() {
    private var binding: ActivityPesananBinding? = null
    private var basePrice = 0
    private var quantity = 1
    private var totalDays: Long = 0 // Store the total days between start and finish


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val productId = intent.getIntExtra("product_id", -1)
        if (productId == -1) {
            Toast.makeText(this, "Product ID tidak valid!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            getProductDetail(productId)
        }

        // Decrease quantity
        binding!!.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updatePrice()
            }
        }

        // Increase quantity
        binding!!.btnIncrease.setOnClickListener {
            quantity++
            updatePrice()
        }

        // Pay Now button action
        binding!!.btnPayNow.setOnClickListener {
            createOrder(productId)  // Call the create order function with product ID
        }

        // Date picker for start date
        binding!!.etDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
                binding!!.etDate.setText(selectedDate)
                calculateTotalDays()
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        // Date picker for end date
        binding!!.etDateFinish.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
                binding!!.etDateFinish.setText(selectedDate)
                calculateTotalDays()
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }
    }

    // Fetch product details
    private fun getProductDetail(productId: Int) {
        val apiService = ApiConfig.getProductDetails().create(ApiService::class.java)
        apiService.getProductById(productId).enqueue(object : Callback<ProductDetailResponse?> {
            override fun onResponse(
                call: Call<ProductDetailResponse?>,
                response: Response<ProductDetailResponse?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { product ->
                        binding?.apply {
                            namaKostum.setText(product.nama_kostum ?: "-")
                            jumlah.setText(product.jumlah?.toString() ?: "0")
                            harga.text = "Rp ${formatCurrency(product.harga)}"

                            Glide.with(this@PesananActivity)
                                .load(ApiConfig.getImageUrl() + product.image)
                                .into(image)
                        }
                        basePrice = product.harga
                        updatePrice()
                    } ?: showToast("Gagal mendapatkan detail produk")
                } else {
                    showToast("Gagal mendapatkan detail produk")
                }
            }

            override fun onFailure(call: Call<ProductDetailResponse?>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    // Update price based on quantity
    private fun updatePrice() {
        binding!!.tvQuantity.text = quantity.toString()

        // Calculate total price: base price * quantity * total days
        val totalPrice = basePrice * quantity * totalDays

        // Set the price to the TextView
        binding!!.tvPrice.text = "Harga: Rp " + formatCurrency(totalPrice.toInt())
    }

    // Create an order after the user has selected the product
    private fun createOrder(kostumId: Int) {
        val tanggalMulai = binding!!.etDate.text.toString()
        val tanggalSelesai = binding!!.etDateFinish.text.toString()

        val formattedTanggalMulai = convertDateFormat(tanggalMulai)
        val formattedTanggalSelesai = convertDateFormat(tanggalSelesai)

        if (formattedTanggalMulai.isEmpty() || formattedTanggalSelesai.isEmpty()) {
            showToast("Tanggal tidak valid!")
            return
        }

        val currentTime = getCurrentTimestamp()

        val orderRequest = PesananKostumRequest(
            kosta_id = kostumId,
            Users_id = 1,  // Replace with actual user ID
            tanggal_pemakaian_mulai = formattedTanggalMulai,
            tanggal_pemakaian_selesai = formattedTanggalSelesai,
            total_harga = basePrice * quantity,
            status_pesanan = "pending",
            updated_at = currentTime,
            created_at = currentTime
        )

        val apiService = ApiConfig.getRetrofitInstance().create(ApiService::class.java)
        apiService.createKostumOrder(orderRequest).enqueue(object : Callback<PesananKostumResponse> {
            override fun onResponse(
                call: Call<PesananKostumResponse>,
                response: Response<PesananKostumResponse>
            ) {
                if (response.isSuccessful) {
                    showToast("Pesanan berhasil dibuat")
                    createPaymentTransaction(basePrice * quantity)  // Call payment after order creation
                } else {
                    showToast("Gagal membuat pesanan")
                }
            }

            override fun onFailure(call: Call<PesananKostumResponse>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    // Create payment transaction after the order is created
    private fun createPaymentTransaction(amount: Int) {
        val token = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            showToast("User not authenticated. Please log in.")
            return
        }

        val paymentRequest = PaymentRequest(amount.toDouble())

        val apiService = ApiConfig.getRetrofitInstance().create(ApiService::class.java)
        apiService.createTransaction("Bearer $token", paymentRequest)
            .enqueue(object : Callback<PaymentResponse?> {
                override fun onResponse(
                    call: Call<PaymentResponse?>,
                    response: Response<PaymentResponse?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        showPaymentSuccessDialog(
                            response.body()!!.snap_token,
                            response.body()!!.order_id
                        )
                    } else {
                        showToast("Gagal membuat transaksi pembayaran")
                    }
                }

                override fun onFailure(call: Call<PaymentResponse?>, t: Throwable) {
                    showToast("Error: ${t.message}")
                }
            })
    }

    // Show success dialog after successful payment
    private fun showPaymentSuccessDialog(snapToken: String, orderId: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Pembayaran Berhasil")
        dialogBuilder.setMessage("Transaksi berhasil dilakukan! \n\nOrder ID: $orderId\nSnap Token: $snapToken")

        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()  // Close the activity after payment success
        }

        dialogBuilder.setCancelable(false)
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    // Show Toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Format currency for display
    private fun formatCurrency(value: Int): String {
        return NumberFormat.getInstance(Locale("id", "ID")).format(value.toLong())
    }

    // Convert date format from dd/MM/yyyy to yyyy-MM-dd
    private fun convertDateFormat(dateStr: String): String {
        try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr)
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            return ""
        }
    }

    // Get current timestamp in the desired format
    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
    }

    // Calculate total days between start and finish dates
    private fun calculateTotalDays() {
        val startDateStr = binding!!.etDate.text.toString()
        val finishDateStr = binding!!.etDateFinish.text.toString()

        if (startDateStr.isNotEmpty() && finishDateStr.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                val startDate = dateFormat.parse(startDateStr)
                val finishDate = dateFormat.parse(finishDateStr)

                if (startDate != null && finishDate != null) {
                    val diffInMillis = finishDate.time - startDate.time
                    val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                    totalDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                    binding!!.hari.text = "$diffInDays"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
