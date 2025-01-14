package com.example.rants

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig.getImageUrl
import com.example.rants.api.ApiConfig.getProductDetails
import com.example.rants.api.ApiConfig.getRetrofitInstance
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityPesananBinding
import com.example.rants.model.PaymentRequest
import com.example.rants.model.PaymentResponse
import com.example.rants.model.PesananKostumRequest
import com.example.rants.model.PesananKostumResponse
import com.example.rants.model.ProductDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PesananActivity : AppCompatActivity() {
    private var binding: ActivityPesananBinding? = null
    private var basePrice = 0
    private var quantity = 1
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)  // Correct binding initialization
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
            goToPembayaranActivity()
        }

        // Setup date and time pickers
//        setupDatePicker(binding!!.etDate)
//        setupTimePicker(binding!!.etTime)
//        setupTimePicker(binding!!.etTimeFinish)
    }

//    private fun setupDatePicker(datePicker: DatePicker) {
//        val currentDate = Calendar.getInstance()
//        datePicker.init(
//            currentDate.get(Calendar.YEAR),
//            currentDate.get(Calendar.MONTH),
//            currentDate.get(Calendar.DAY_OF_MONTH)
//        ) { _, year, month, dayOfMonth ->
//            // Handle date selection
//            val selectedDate = "$dayOfMonth/${month + 1}/$year"
//            binding!!.etDate.setText(selectedDate)
//        }
//    }

//    private fun setupTimePicker(timePicker: TimePicker) {
//        val currentTime = Calendar.getInstance()
//        timePicker.setIs24HourView(true)
//        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
//            // Mengupdate waktu yang dipilih
//            val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
//            if (timePicker == binding!!.etTimestar) {
//                binding!!.etTimestar.setText(formattedTime)
//            } else if (timePicker == binding!!.etTimeFinish) {
//                binding!!.etTimeFinish.setText(formattedTime)
//            }
//        }
//    }

    // Function to fetch product details
    private fun getProductDetail(productId: Int) {
        val apiService = getProductDetails().create(ApiService::class.java)
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
                                .load(getImageUrl() + product.image)
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
        binding!!.tvPrice.text = "Harga: Rp " + formatCurrency(basePrice * quantity)
    }

    // Create order function
    private fun createOrder(kostumId: Int) {
        val formattedDate = convertDateFormat(binding!!.etDatestar.toString())
        if (formattedDate.isEmpty()) {
            showToast("Tanggal tidak valid!")
            return
        }

        val orderRequest = PesananKostumRequest(
            kostumId,
            1,  // Replace with actual user ID
            formattedDate,
            formattedDate,
            basePrice * quantity,
            "Berhasil"
        )

        val apiService = getRetrofitInstance().create(ApiService::class.java)
        apiService.createKostumOrder(orderRequest).enqueue(object : Callback<PesananKostumResponse> {
            override fun onResponse(
                call: Call<PesananKostumResponse>,
                response: Response<PesananKostumResponse?>
            ) {
                if (response.isSuccessful) {
                    showToast("Pesanan berhasil dibuat")
                    createPaymentTransaction(basePrice * quantity)
                } else {
                    showToast("Gagal membuat pesanan")
                }
            }

            override fun onFailure(call: Call<PesananKostumResponse>, t: Throwable) {
                showToast("Error: " + t.message)
            }
        })
    }

    // Create payment transaction
    private fun createPaymentTransaction(amount: Int) {
        val token = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            .getString("token", null)

        if (token == null) {
            showToast("User not authenticated. Please log in.")
            return
        }

        val paymentRequest = PaymentRequest(amount.toDouble())

        val apiService = getRetrofitInstance().create(ApiService::class.java)
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
                    showToast("Error: " + t.message)
                }
            })
    }

    // Navigate to PembayaranActivity
    private fun goToPembayaranActivity() {
        startActivity(Intent(this, DetailActivity::class.java))
    }

    // Show payment success dialog
    private fun showPaymentSuccessDialog(snapToken: String, orderId: String) {
        AlertDialog.Builder(this)
            .setTitle("Pembayaran Berhasil")
            .setMessage("Transaksi berhasil! \nOrder ID: $orderId\nSnap Token: $snapToken")
            .setPositiveButton(
                "OK"
            ) { dialog: DialogInterface?, which: Int -> finish() }
            .setCancelable(false)
            .show()
    }

    // Format currency
    private fun formatCurrency(value: Int): String {
        return NumberFormat.getInstance(Locale("id", "ID")).format(value.toLong())
    }

    // Show Toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Convert date format
    private fun convertDateFormat(dateStr: String): String {
        try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateStr)
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            return ""
        }
    }
}
