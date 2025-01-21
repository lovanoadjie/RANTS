package com.example.rants

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rants.adapter.CalendarAdapter
import com.example.rants.api.ApiConfig
import com.example.rants.api.ApiService
import com.example.rants.model.CalendarModel
import com.example.rants.databinding.ActivityJadwalBinding
import com.example.rants.helper.EventDecorator
import com.example.rants.model.CalendarAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class JadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalBinding
    private var selectedDate: String = ""
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize BottomSheetBehavior
        bottomSheetBehavior = BottomSheetBehavior.from(binding.sheet)
        bottomSheetBehavior.apply {
            peekHeight = 350   // Set peek height
            state = BottomSheetBehavior.STATE_COLLAPSED  // Set initial state
        }

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup MaterialCalendarView untuk memilih tanggal
        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            // Pastikan objek date adalah CalendarDay dan ambil objek Date
            val calendar = Calendar.getInstance()  // Menggunakan getInstance dari Calendar
            calendar.set(date.year, date.month - 1, date.day)  // Perhatikan bahwa bulan di Calendar dimulai dari 0

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

        // Panggil API untuk mendapatkan data acara saat Activity dibuat
        fetchAllCalendarData()
    }

    // Fungsi untuk mendapatkan semua data acara dan menambahkan dekorator
    private fun fetchAllCalendarData() {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)

        apiService.getAllCalendar().enqueue(object : Callback<List<CalendarAll>> {
            override fun onResponse(call: Call<List<CalendarAll>>, response: Response<List<CalendarAll>>) {
                if (response.isSuccessful) {
                    val calendars = response.body()
                    if (!calendars.isNullOrEmpty()) {
                        addEventDecorators(calendars)
                    }
                } else {
                    Toast.makeText(this@JadwalActivity, "Gagal mendapatkan data: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarAll>>, t: Throwable) {
                Toast.makeText(this@JadwalActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk mendapatkan data berdasarkan tanggal
    private fun fetchCalendarData(date: String) {
        val apiService = ApiConfig.getRetrofit().create(ApiService::class.java)
        val call = apiService.getCalendars(date)

        call.enqueue(object : Callback<List<CalendarModel>> {
            override fun onResponse(call: Call<List<CalendarModel>>, response: Response<List<CalendarModel>>) {
                if (response.isSuccessful) {
                    val calendars = response.body()
                    if (!calendars.isNullOrEmpty()) {
                        // Tampilkan BottomSheet jika ada acara
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        setupRecyclerView(calendars)
                    } else {
                        // Sembunyikan BottomSheet jika tidak ada acara
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        Toast.makeText(this@JadwalActivity, "Tidak ada acara pada tanggal ini", Toast.LENGTH_SHORT).show()

                        // Clear content/recyclerView jika tidak ada acara
                        clearRecyclerView()
                    }
                } else {
                    Toast.makeText(this@JadwalActivity, "Gagal mendapatkan data: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarModel>>, t: Throwable) {
                Toast.makeText(this@JadwalActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk meng-clear/recyclerView ketika tidak ada acara
    private fun clearRecyclerView() {
        val recyclerView = binding.eventRecyclerView
        val adapter = CalendarAdapter() // Instantiate a new adapter to reset the content
        recyclerView.adapter = adapter // Clear the content
    }

    // Menambahkan dekorator untuk tanggal yang memiliki acara
    private fun addEventDecorators(calendars: List<CalendarAll>) {
        val dates = calendars.map {
            // Ubah tanggal menjadi CalendarDay
            val dateParts = it.date.split("-")
            CalendarDay.from(dateParts[0].toInt(), dateParts[1].toInt(), dateParts[2].toInt())
        }

        binding.calendarView.addDecorator(EventDecorator(Color.RED, dates))
    }

    // Setup RecyclerView untuk menampilkan daftar acara
    private fun setupRecyclerView(calendars: List<CalendarModel>) {
        val recyclerView = binding.eventRecyclerView

        // Set layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set adapter
        val adapter = CalendarAdapter() // Instantiate your adapter here
        adapter.setData(calendars) // Pass data to the adapter
        recyclerView.adapter = adapter
    }
}
