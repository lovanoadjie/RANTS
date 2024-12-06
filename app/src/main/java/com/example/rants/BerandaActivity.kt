package com.example.rants

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.rants.databinding.ActivityBerandaBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BerandaActivity : AppCompatActivity() {

    // Binding untuk layout
    private lateinit var binding: ActivityBerandaBinding
    private lateinit var adapter: ImageAdapter
    private val listGambar = ArrayList<ImageData>()
    private lateinit var dots: ArrayList<TextView>
private val slideHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(0, 0)

//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)


        listGambar.add(
            ImageData(
                imgUrl = "https://images.unsplash.com/photo-1515041219749-89347f83291a?q=80&w=774&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            )
        )
        listGambar.add(
            ImageData(
                imgUrl = "https://images.unsplash.com/photo-1660080494538-bc62fbc6a30d?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8Y2FydG9vbnN8ZW58MHx8MHx8fDA%3D"
            )
        )
        listGambar.add(
            ImageData(
                imgUrl = "https://images.unsplash.com/photo-1717732596477-04f8c5d53387?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Y2FydG9vbnN8ZW58MHx8MHx8fDA%3D"
            )
        )
        adapter = ImageAdapter(listGambar)
        binding.viewPager.adapter = adapter
        dots = ArrayList()
        setIndicator()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                selectDot(position)
                super.onPageSelected(position)

                slideHandler.removeCallbacks(sliderRun)
                slideHandler.postDelayed(sliderRun,3000)
            }
        })


        }

    private val sliderRun = Runnable {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    private fun selectDot(position: Int) {
        for (i in 0 until listGambar.size) {
            if (i==position){
              dots[i].setTextColor(ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_primary))
            }else{
                dots[i].setTextColor(ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_secondary))
            }
        }
    }
    private fun setIndicator() {
        for (i in 0 until listGambar.size) {
            dots.add(TextView(this))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            }else{
                dots[i].text = Html.fromHtml("&#9679")
            }
            dots[i].textSize = 18f
            binding.dotsIndecator.addView(dots[i])
        }
        setupBottomNavigation()
    }
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_pesan-> {
                    startActivity(Intent(this, PesanActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_riwayat-> {
                    startActivity(Intent(this, RiwayatActyvity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_profil-> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }

    }

}
