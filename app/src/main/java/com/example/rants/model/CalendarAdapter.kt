package com.example.rants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rants.model.Calendar

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private val calendarEvents = mutableListOf<Calendar>()

    fun setData(data: List<Calendar>) {
        calendarEvents.clear()
        calendarEvents.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = calendarEvents[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = calendarEvents.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

        fun bind(event: Calendar) {
            titleText.text = event.title
            dateText.text = event.date
            descriptionText.text = event.description ?: "Tidak ada deskripsi"
        }
    }
}
