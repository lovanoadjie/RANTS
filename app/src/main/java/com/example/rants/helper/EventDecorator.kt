package com.example.rants.helper

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDecorator(private val color: Int, dates: Collection<CalendarDay>) :
    com.prolificinteractive.materialcalendarview.DayViewDecorator {

    private val dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5f, color)) // Menambahkan titik merah pada tanggal
    }
}