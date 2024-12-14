package com.example.rants.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig
import com.example.rants.databinding.ItemMakeupBinding
import com.example.rants.model.Makeup
class MakeupAdapter(private val makeupList: List<Makeup>) : RecyclerView.Adapter<MakeupAdapter.MakeupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeupViewHolder {
        val binding = ItemMakeupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MakeupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MakeupViewHolder, position: Int) {
        val makeup = makeupList[position]
//        val baseUrl = "http://192.168.137.128:8000/storage/" // Replace with your actual API base URL
        val imageUrl = ApiConfig.getImageUrl() + makeup.image // Concatenate base URL with image path
        Log.d("MakeupAdapter", "Image URL: $imageUrl")

        holder.bind(makeup, imageUrl) // Pass the full image URL to bind()
    }

    override fun getItemCount(): Int = makeupList.size

    // ViewHolder for Makeup item
    class MakeupViewHolder(private val binding: ItemMakeupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(makeup: Makeup, imageUrl: String) {
            // Null check for kategory
            val category = makeup.Kategory?.name ?: "Unknown Category" // Default to "Unknown Category" if kategory is null
            binding.kategori.text = category

            // Set the price
            binding.harga.text = makeup.harga.toString()

            // Use Glide to load the image from the complete URL
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.image)
        }
    }
}
