package com.example.rants.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rants.model.Product
import com.example.rants.databinding.ItemKostumBinding

// Adapter untuk RecyclerView
class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemKostumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    // ViewHolder untuk CardView item
    class ProductViewHolder(private val binding: ItemKostumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.title.text = product.title
            binding.price.text = product.price.toString()

            // Menggunakan Glide untuk memuat gambar dari URL
            Glide.with(binding.root.context)
                .load(product.image)
                .into(binding.image)
        }
    }
}
