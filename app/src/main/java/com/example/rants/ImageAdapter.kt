package com.example.rants

import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rants.databinding.ItemImageBinding
import com.example.rants.databinding.ListSlideBinding
import com.example.rants.model.Gallery
class ImageAdapter(private val listGambar: ArrayList<Gallery>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // Mengatur ukuran layout sesuai match_parent
        val layoutParams = binding.root.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.root.layoutParams = layoutParams

        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val gallery = listGambar[position]
        val baseUrl =  "http://192.168.208.91:8000/storage/" // Replace with your actual API base URL
        val imageUrl = baseUrl + gallery.image
        // Assuming you're using Glide to load images
        Log.d("ImageAdapter", "Image URL: $imageUrl") // Log the image URL

        Glide.with(holder.itemView.context)
            .load(imageUrl)  // Image URL
            .into(holder.binding.imageView)  // Bind the image to ImageView

        // If you want to display descriptions, you can bind them too
//        holder.binding.textViewDescription.text = gallery.description
    }

    override fun getItemCount(): Int = listGambar.size

    class ImageViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)
}
