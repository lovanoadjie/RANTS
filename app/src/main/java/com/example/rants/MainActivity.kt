package com.example.rants

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Animasi untuk LinearLayout "keatas"
        val slideUp = ObjectAnimator.ofFloat(binding.keatas, "translationY", 800f, 0f).apply {
            duration = 1000 // Durasi animasi 1 detik
            interpolator = DecelerateInterpolator() // Ease-out effect
        }
        val fadeIn = ObjectAnimator.ofFloat(binding.keatas, "alpha", 0f, 1f).apply {
            duration = 1500 // Durasi animasi 1 detik
        }

        // Gabungkan animasi
        AnimatorSet().apply {
            playTogether(slideUp, fadeIn)
            start()
        }

        // Tombol untuk masuk ke LoginActivityvv
        binding.startButton.setOnClickListener {
            try {
                goToLoginActivity()
            } catch (e: Exception) {
                e.printStackTrace() // Log error jika terjadi pengecualian
            }
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
