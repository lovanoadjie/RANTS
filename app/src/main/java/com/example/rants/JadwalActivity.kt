package com.example.rants

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rants.adapter.CalendarAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.model.Calendar
import com.example.rants.databinding.ActivityJadwalBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class JadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalBinding
    private lateinit var selectedDate: String // Menyimpan tanggal yang dipilih

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.sheet)
        // Apply settings to the BottomSheetBehavior
        bottomSheetBehavior.apply {
            peekHeight = 350   // Set peek height
            state = BottomSheetBehavior.STATE_COLLAPSED  // Set initial state
        }

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup CalendarView untuk memilih tanggal
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth -> val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            // Format tanggal yang dipilih menjadi string
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)

            // Panggil API dengan tanggal yang dipilih
            fetchCalendarData(selectedDate)
        }

        // Setup Toolbar Back Button
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    // Fungsi untuk mendapatkan data berdasarkan tanggal
    private fun fetchCalendarData(date: String) {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        val call = apiService.getCalendars(date)

        call.enqueue(object : Callback<List<Calendar>> {
            override fun onResponse(call: Call<List<Calendar>>, response: Response<List<Calendar>>) {
                if (response.isSuccessful) {
                    val calendars = response.body()
                    if (!calendars.isNullOrEmpty()) {
                        setupRecyclerView(calendars)
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

    // Setup RecyclerView
    private fun setupRecyclerView(calendars: List<Calendar>) {
        val recyclerView = binding.eventRecyclerView

        // Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set adapter
        val adapter = CalendarAdapter() // Instantiate your adapter here
        adapter.setData(calendars) // Pass data to the adapter
        recyclerView.adapter = adapter
    }
}