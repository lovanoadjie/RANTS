package com.example.rants.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rants.api.ApiConfig
import com.example.rants.model.kosta
import com.example.rants.databinding.ItemKostumBinding
import com.example.rants.formatRupiah
import java.text.NumberFormat
import java.util.Locale

// Adapter untuk RecyclerView
class ProductAdapter(private val productList: List<kosta>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var onItemClick: ((kosta) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemKostumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        val imageUrl = ApiConfig.getImageUrl() + product.image
        Log.d("ImageAdapter", "Image URL: $imageUrl")

        holder.bind(product, imageUrl)

        // Handle click on the item
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product) // Pass the clicked product to the Activity
        }
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(private val binding: ItemKostumBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: kosta, imageUrl: String) {
            binding.namaKostum.text = product.nama_kostum
//            binding.jumlah.text = product.jumlah.toString()
//            binding.warna.text = product.warna
            binding.harga.text =  "Rp ${formatCurrency(product.harga ?: 0)}"

            // Menggunakan Glide untuk memuat gambar dari URL lengkap
            Glide.with(binding.root.context)
                .load(imageUrl)
                .into(binding.image)
        }

        private fun formatCurrency(value: Int): String {
            val numberFormat = NumberFormat.getInstance(Locale("id", "ID"))
            return numberFormat.format(value)
        }
    }
}
