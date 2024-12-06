package com.example.rants

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rants.databinding.ActivityWhaynotcarouselBinding
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class WhaynotcarouselActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWhaynotcarouselBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhaynotcarouselBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val carousel: ImageCarousel = findViewById(R.id.carousel)

// Register lifecycle. For activity this will be lifecycle/getLifecycle() and for fragment it will be viewLifecycleOwner/getViewLifecycleOwner().
        carousel.registerLifecycle(lifecycle)

        val list = mutableListOf<CarouselItem>()

// Image URL with caption
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1619033742043-b9a1adf35b30?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8cGl4YWJheXxlbnwwfHwwfHx8MA%3D%3D",
                caption = "Pemandangan"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1636625333282-342ce0136f06?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHBpeGFiYXl8ZW58MHx8MHx8fDA%3D",
                caption = "kelinci"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1619489934304-5d2e5bf8703b?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTd8fHBpeGFiYXl8ZW58MHx8MHx8fDA%3D",
                caption = "Bunga"
            )
        )
        carousel.setData(list)
    }
}