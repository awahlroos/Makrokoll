package se.umu.cs.oi19aws.makrokoll.models

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecorator(private var columnGapSize:Int, private var paddingContainerBottom:Int)
    : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position % 2 == 0) {
            outRect.right = columnGapSize
        } else {
            outRect.left = columnGapSize
        }
        if (position == state.itemCount - 1) {
            outRect.bottom = paddingContainerBottom
        }
    }
}