package com.example.rants.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rants.R
import com.example.rants.model.ItemRiwayat

class RiwayatAdapter(private val dataList: List<ItemRiwayat>) :
    RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    // ViewHolder untuk menghubungkan tampilan item_riwayat.xml
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val productTotal: TextView = itemView.findViewById(R.id.product_total)
        val buttonBuyAgain: Button = itemView.findViewById(R.id.button_buy_again)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.productImage.setImageResource(item.imageResId)
        holder.productName.text = item.name
        holder.productPrice.text = item.price
        holder.productTotal.text = item.total
        holder.buttonBuyAgain.setOnClickListener {
            // Logika untuk tombol
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}