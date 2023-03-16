package se.umu.cs.oi19aws.makrokoll.models

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

//Class to provide offset for columns in recycler views
class SpacesItemDecorator(private var columnCount:Int, private var columnGapSize:Int, private var paddingContainerBottom:Int)
    : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if(columnCount == 2) {
            if (position % 2 == 0) {
                outRect.right = columnGapSize
            } else {
                outRect.left = columnGapSize
            }
            if (position == state.itemCount - 1) {
                outRect.bottom = paddingContainerBottom
            }
        } else if(columnCount == 3){
            // Set the left and right offsets based on the current column
            when (position % columnCount) {
                0 -> {
                    outRect.left = 0
                    outRect.right = columnGapSize
                }
                1 -> {
                    outRect.left = columnGapSize / 2
                    outRect.right = columnGapSize / 2
                }
                2 -> {
                    outRect.left = columnGapSize
                    outRect.right = 0
                }
            }

            // Set the bottom offset for the last row
            val rowCount = ceil(state.itemCount.toFloat() / columnCount).toInt()
            val row = position / columnCount
            if (row == rowCount - 1) {
                outRect.bottom = paddingContainerBottom
            } else {
                outRect.bottom = 0
            }
        }
    }
}