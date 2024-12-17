package com.example.rants

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rants.adapter.ProductAdapter
import com.example.rants.adapter.TariAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.databinding.ActivityJasatariBinding
import com.example.rants.databinding.ActivityKostumBinding
import com.example.rants.model.ProductResponse
import com.example.rants.model.Tari
import com.example.rants.model.TariResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JasatariActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJasatariBinding
    private lateinit var tariAdapter: TariAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJasatariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        getTariFromApi()
    }

    private fun getTariFromApi() {
        Log.d("API Response", "Fetching products...")
        val apiService = ApiConfig.getTari().create(ApiService::class.java)
        apiService.getTari().enqueue(object : Callback<TariResponse> {
            override fun onResponse(call: Call<TariResponse>, response: Response<TariResponse>) {
                if (response.isSuccessful) {
                    val tari = response.body()?.data
//                    if (responseBody != null && responseBody.data.isNotEmpty()) {
//                        // Now use responseBody.data to populate the RecyclerView
//                        tariAdapter = TariAdapter(responseBody.data)
//                        binding.recyclerView.layoutManager =
//                            LinearLayoutManager(this@JasatariActivity)
//                        binding.recyclerView.adapter = tariAdapter
//                    } else {
//                        Log.d("API Response", "No item found")
//                        Toast.makeText(
//                            this@JasatariActivity,
//                            "Tidak ada produk ditemukan",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    Log.e("API Error", "Error: ${response.code()} - ${response.message()}")
//                    Toast.makeText(
//                        this@JasatariActivity,
//                        "Gagal memuat produk: ${response.message()}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }

                    if (tari != null) {
                        // Simpan ke adapter dan set onItemClick
                        tariAdapter = TariAdapter(tari).apply {
                            onItemClick = { Tari ->
                                val intent =
                                    Intent(this@JasatariActivity, DetailTariActivity::class.java)
                                intent.putExtra("tari_id", Tari.id)  // Mengirimkan ID produk
                                startActivity(intent)
                            }
                        }
                        binding.recyclerView.adapter = tariAdapter
                    }
                }
            }


            override fun onFailure(call: Call<TariResponse>, t: Throwable) {
                Log.e("Jasatari Activity", "Error: ${t.message}")
                t.printStackTrace() // Mencetak stack trace untuk debugging lebih lanjut
                Toast.makeText(this@JasatariActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                onBackPressed()  // Go back to the previous screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}