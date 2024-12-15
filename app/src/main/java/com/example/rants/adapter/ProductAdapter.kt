package com.example.rants.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rants.model.kosta
import com.example.rants.databinding.ItemKostumBinding

// Adapter untuk RecyclerView
class ProductAdapter(private val productList: List<kosta>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemKostumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        val baseUrl =  "http://192.168.137.128:8000/storage/" // Replace with your actual API base URL
        val imageUrl = baseUrl + product.image // Concatenate base URL with image path
        Log.d("ImageAdapter", "Image URL: $imageUrl")

        holder.bind(product, imageUrl) // Pass the full image URL to bind()
    }

    override fun getItemCount(): Int = productList.size

    // ViewHolder untuk CardView item
    class ProductViewHolder(private val binding: ItemKostumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: kosta, imageUrl: String) {
            binding.namaKostum.text = product.nama_kostum
            binding.jumlah.text = product.jumlah.toString()
            binding.warna.text = product.warna
            binding.ukuran.text = product.ukuran
            binding.harga.text = product.harga.toString()

            // Menggunakan Glide untuk memuat gambar dari URL lengkap
            Glide.with(binding.root.context)
                .load(imageUrl) // Use the complete image URL
                .into(binding.image)
        }
    }
}
