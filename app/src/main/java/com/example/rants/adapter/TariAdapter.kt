package com.example.rants.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rants.databinding.ItemTariBinding
import com.example.rants.model.Tari

class TariAdapter(private val tariList: List<Tari>) : RecyclerView.Adapter<TariAdapter.TariViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariViewHolder {
        val binding = ItemTariBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TariViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TariViewHolder, position: Int) {
        val tari = tariList[position]
        val baseUrl =  "http://192.168.137.94:8000/storage/" // Replace with your actual API base URL
        val imageUrl = baseUrl + tari.image // Concatenate base URL with image path
        Log.d("TariAdapter", "Image URL: $imageUrl")

        holder.bind(tari, imageUrl) // Pass the full image URL to bind()
    }

    override fun getItemCount(): Int = tariList.size

    class TariViewHolder(private val binding: ItemTariBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tari: Tari, imageUrl: String) {
            binding.namaTari.text = tari.jenis_tarian
            binding.jumlahPenari.text = tari.jumlah_penari.toString()
            binding.deskripsi.text = tari.deskripsi_acara
            binding.harga.text = tari.harga.toString()

            // Menggunakan Glide untuk memuat gambar dari URL lengkap
            Glide.with(binding.root.context)
                .load(imageUrl) // Use the complete image URL
                .into(binding.image)
        }
    }

}