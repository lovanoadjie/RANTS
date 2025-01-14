package com.example.rants

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(value: Int): String {
    val numberFormat = NumberFormat.getInstance(Locale("id", "ID"))
    numberFormat.maximumFractionDigits = 0 // Hilangkan angka desimal
    return numberFormat.format(value)
}