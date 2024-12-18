package com.example.rants.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.engine.GlideException
import com.example.rants.api.ApiConfig
import com.example.rants.databinding.ItemMakeupBinding
import com.example.rants.model.Makeup

class MakeupAdapter(private val makeupList: List<Makeup>) : RecyclerView.Adapter<MakeupAdapter.MakeupViewHolder>() {

    var onItemClick: ((Makeup) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeupViewHolder {
        val binding = ItemMakeupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MakeupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MakeupViewHolder, position: Int) {
        val makeup = makeupList[position]
        val imageUrl = ApiConfig.getImageUrl() + makeup.image // Concatenate base URL with image path
        Log.d("MakeupAdapter", "Image URL: $imageUrl")

        holder.bind(makeup, imageUrl) // Pass the full image URL to bind()

        // Handle click on the item
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(makeup) // Pass the clicked product to the Activity
        }
    }

    override fun getItemCount(): Int = makeupList.size

    // ViewHolder for Makeup item
    class MakeupViewHolder(private val binding: ItemMakeupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(makeup: Makeup, imageUrl: String) {
            // Null check for category
            val category = makeup.Kategory?.name
                ?: "Unknown Category" // Default to "Unknown Category" if category is null
            binding.kategory.text = category

            // Set the price
            binding.harga.text = makeup.harga.toString()

            // Use Glide to load the image from the complete URL
            Glide.with(binding.root)
                .load(imageUrl)
//                .addListener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?, model: Any?, target: Target<Drawable>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        e?.logRootCauses("Glide Load Error")
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable?, model: Any?, target: Target<Drawable>?,
//                        dataSource: DataSource?, isFirstResource: Boolean
//                    ): Boolean {
//                        return false // Glide akan menangani setelah gambar siap
//                    }
//                })
                .into(binding.image)

        }
    }}
