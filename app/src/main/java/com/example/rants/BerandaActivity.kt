package com.example.rants

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.MediaController
import android.widget.TextView
import android.widget.TimePicker
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityBerandaBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.*

class BerandaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBerandaBinding
    private val clock: TextView by lazy { findViewById(R.id.digitalClock) }  // Using val for clock
    private val handler = Handler()
    private var currentTimeInMillis: Long = System.currentTimeMillis()  // Set initial time

    // Runnable to update time every second
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            currentTimeInMillis = System.currentTimeMillis()  // Update the current time every second
            updateClock()  // Update clock display
            updateDate()   // Update date display
            handler.postDelayed(this, 1000)  // Continue updating every 1 second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jadwalButton.setOnClickListener {
            goToJadwalActivity()
        }
    }

    private fun goToJadwalActivity() {
        val intent = Intent(this, JadwalActivity::class.java).also {
            startActivity(it)
        }

        val videoViewMek: VideoView = findViewById(R.id.videoViewMek)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoViewMek)

       val offlineUri:Uri = Uri.parse("android.resource://$packageName/${R.raw.video}")
        videoViewMek.setMediaController(mediaController)
        videoViewMek.setVideoURI(offlineUri  )
        videoViewMek.requestFocus()
        videoViewMek.start()

        // Reference to BottomSheetBehavior
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.sheet)
        bottomSheetBehavior.apply {
            peekHeight = 500   // Set peek height
            state = BottomSheetBehavior.STATE_COLLAPSED  // Set initial state
        }

        // Reference to BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set item selected listener for bottom navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_beranda -> {
                    // Navigate to BerandaActivity
                    startActivity(Intent(this, BerandaActivity::class.java))
                    true
                }
                R.id.bottom_pesan -> {
                    // Navigate to PesanActivity
                    startActivity(Intent(this, PesanActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Start updating the time
        handler.post(updateTimeRunnable)

        // Set up TimePickerDialog on TextView click to manually set time
        clock.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Show TimePickerDialog to set time manually
            val timePickerDialog = TimePickerDialog(this, { view, hourOfDay, minuteOfHour ->
                // Set the chosen time manually
                currentTimeInMillis = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minuteOfHour)
                }.timeInMillis
                updateClock()  // Update the clock display after setting time manually
            }, hour, minute, true)

            timePickerDialog.show()
        }
    }

    // Method to update the clock with the current time
    private fun updateClock() {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())  // Format for time
        val currentTime = sdf.format(Date(currentTimeInMillis))

        // Update the clock TextView with the current time
        clock.text = currentTime
    }

    // Method to update the date with the current date
    private fun updateDate() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())  // Format for date
        val currentDate = sdf.format(Date(currentTimeInMillis))

        // Update the date TextView with the current date
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeRunnable) // Stop the time updates when the activity is destroyed
    }
}
