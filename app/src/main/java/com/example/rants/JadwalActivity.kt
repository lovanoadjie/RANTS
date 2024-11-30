package com.example.rants

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.api.ApiConfig
import com.example.rants.model.Calendar
import com.example.rants.databinding.ActivityJadwalBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup BottomSheetBehavior
        val bottomSheet = findViewById<FrameLayout>(R.id.sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Setup Toolbar Back Button
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Ambil data jadwal dari API
        fetchCalendarData()
    }

    private fun fetchCalendarData() {
        val apiService = ApiConfig.getRetrofit().create(com.example.rants.api.ApiService::class.java)
        val call = apiService.getCalendars()

        call.enqueue(object : Callback<List<Calendar>> {
            override fun onResponse(call: Call<List<Calendar>>, response: Response<List<Calendar>>) {
                if (response.isSuccessful) {
                    val calendars = response.body()
                    if (calendars != null && calendars.isNotEmpty()) {
                        setupSpinner(calendars)
                    } else {
                        Toast.makeText(this@JadwalActivity, "Tidak ada data jadwal", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@JadwalActivity, "Gagal mendapatkan data: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Calendar>>, t: Throwable) {
                Toast.makeText(this@JadwalActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSpinner(calendars: List<Calendar>) {
        val eventTitles = calendars.map { "${it.title} - ${it.date}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, eventTitles)
        binding.eventSpinner.adapter = adapter

        binding.eventSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedEvent = calendars[position]
                Toast.makeText(
                    this@JadwalActivity,
                    "Judul: ${selectedEvent.title}\nTanggal: ${selectedEvent.date}\nDeskripsi: ${selectedEvent.description}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Tidak ada aksi
            }
        }
    }
}
