package com.example.rants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rants.R
import com.example.rants.databinding.ItemRiwayatBinding
//import com.example.rants.model.ItemRiwayat
import com.example.rants.model.PesananKostum

class RiwayatAdapter(private val list: List<PesananKostum>) :
    RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRiwayatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvNamaProduk.text = "Kostum ID: ${item.kosta_id}"
        holder.binding.tvHarga.text = "Rp${item.total_harga}"
        holder.binding.tvJumlah.text = "Status: ${item.status_pesanan}"
        // Gambar bisa disesuaikan kalau ada gambar dari API
    }
}
