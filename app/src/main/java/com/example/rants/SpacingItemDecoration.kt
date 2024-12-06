package com.example.rants
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Menambahkan jarak di atas setiap item
        outRect.top = spacing
        outRect.bottom = spacing
        // Menambahkan jarak di bawah item terakhir
        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
            outRect.bottom = spacing
        }
    }
}
