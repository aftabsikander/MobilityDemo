package com.aftabsikander.mercari.ui.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.*


/***
 * Equal Spacing item decoration which handles equal spacing on each sides of item.
 */
class ListSpacingDecoration : RecyclerView.ItemDecoration {
    private var orientation = -1
    private var spanCount = -1
    private var spacing: Int = 0
    private var halfSpacing: Int = 0

    /***
     * Constructor which handles spacing decoration configurations.
     *
     * @param context Calling context.
     * @param spacingDimen Dimension values which holds spacing offset value.
     */
    constructor(context: Context, @DimenRes spacingDimen: Int) {

        spacing = context.resources.getDimensionPixelSize(spacingDimen)
        halfSpacing = spacing / 2
    }

    /**
     * Constructor which handles spacing decoration configurations.
     *
     * @param spacingPx Pixel value which needs to be used for spacing.
     */
    constructor(spacingPx: Int) {
        spacing = spacingPx
        halfSpacing = spacing / 2
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)

        if (orientation == -1) {
            orientation = getOrientation(parent)
        }

        if (spanCount == -1) {
            spanCount = getTotalSpan(parent)
        }

        val childCount = parent.layoutManager!!.itemCount
        val childIndex = parent.getChildAdapterPosition(view)

        val itemSpanSize = getItemSpanSize(parent, childIndex)
        val spanIndex = getItemSpanIndex(parent, childIndex)

        /* INVALID SPAN */
        if (spanCount < 1) return

        setSpacings(outRect, parent, childCount, childIndex, itemSpanSize, spanIndex)
    }

    //region General helper method

    /***
     * Calculate Spacing bounds for item according to span and child counts.
     * @param outRect View Rect bounds.
     * @param parent Recycle view which holds items.
     * @param childCount total child counts.
     * @param childIndex current child index.
     * @param itemSpanSize span size
     * @param spanIndex span index for current item.
     */
    private fun setSpacings(
        outRect: Rect, parent: RecyclerView, childCount: Int,
        childIndex: Int, itemSpanSize: Int, spanIndex: Int
    ) {

        outRect.top = halfSpacing
        outRect.bottom = halfSpacing
        outRect.left = halfSpacing
        outRect.right = halfSpacing

        if (isTopEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.top = spacing
        }

        if (isLeftEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.left = spacing
        }

        if (isRightEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.right = spacing
        }

        if (isBottomEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.bottom = spacing
        }
    }

    /***
     * Get Recycle view layout manager orientation.
     * @param parent Recycle View object.
     * @return layout manager orientation.
     */
    private fun getOrientation(parent: RecyclerView): Int {

        val mgr = parent.layoutManager
        if (mgr is LinearLayoutManager) {
            return mgr.orientation
        } else if (mgr is GridLayoutManager) {
            return mgr.orientation
        } else if (mgr is StaggeredGridLayoutManager) {
            return mgr.orientation
        }
        return VERTICAL
    }
    //endregion

    //region Helper methods for Calculation Span according to total item counts and index

    /***
     * Calculate total span counts which are inside recycle view
     * @param parent RecycleView object.
     * @return span count which is hold inside recycle view layout manager.
     */
    private fun getTotalSpan(parent: RecyclerView): Int {

        val mgr = parent.layoutManager
        if (mgr is GridLayoutManager) {
            return mgr.spanCount
        } else if (mgr is StaggeredGridLayoutManager) {
            return mgr.spanCount
        } else if (mgr is LinearLayoutManager) {
            return 1
        }

        return -1
    }

    /***
     * Get Current Item view span count inside layout manager.
     * @param parent  RecycleView object.
     * @param childIndex Current view item position.
     * @return updated Item span size for current child position.
     */
    private fun getItemSpanSize(parent: RecyclerView, childIndex: Int): Int {

        val mgr = parent.layoutManager
        if (mgr is GridLayoutManager) {
            return mgr.spanSizeLookup.getSpanSize(childIndex)
        } else if (mgr is StaggeredGridLayoutManager) {
            return 1
        } else if (mgr is LinearLayoutManager) {
            return 1
        }

        return -1
    }

    /***
     * Get Item span index according to view position index.
     * @param parent  RecycleView object.
     * @param childIndex Current view item position.
     * @return updated Item span size for current child position.
     */
    private fun getItemSpanIndex(parent: RecyclerView, childIndex: Int): Int {

        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            return layoutManager.spanSizeLookup
                .getSpanIndex(childIndex, spanCount)
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return childIndex % spanCount
        } else if (layoutManager is LinearLayoutManager) {
            return 0
        }

        return -1
    }
    //endregion

    //region Helper methods for span bound calculation

    /***
     * Validate if edge is left bound or not. If it is left bound edge we
     * would perform some spacing calculation according to span size and index.
     * @param parent Recycle view object.
     * @param childCount Total item counts.
     * @param childIndex Current item index.
     * @param itemSpanSize Total item span size.
     * @param spanIndex current item span index.
     *
     * @return calculated Left edge bound.
     */
    private fun isLeftEdge(
        parent: RecyclerView, childCount: Int, childIndex: Int,
        itemSpanSize: Int, spanIndex: Int
    ): Boolean {

        return if (orientation == VERTICAL) {
            spanIndex == 0
        } else {
            childIndex == 0 || isFirstItemEdgeValid(childIndex < spanCount, parent, childIndex)
        }
    }

    /***
     * Validate if edge is Right bound or not. If it is right bound edge we
     * would perform some spacing calculation according to span size and index.
     * @param parent Recycle view object.
     * @param childCount Total item counts.
     * @param childIndex Current item index.
     * @param itemSpanSize Total item span size.
     * @param spanIndex current item span index.
     *
     * @return calculated right edge bound.
     */
    private fun isRightEdge(
        parent: RecyclerView, childCount: Int, childIndex: Int,
        itemSpanSize: Int, spanIndex: Int
    ): Boolean {

        return if (orientation == VERTICAL) {
            spanIndex + itemSpanSize == spanCount
        } else {
            isLastItemEdgeValid(
                childIndex >= childCount - spanCount,
                parent, childCount, childIndex, spanIndex
            )
        }
    }

    /***
     * Validate if edge is Top bound or not. If it is Top bound edge we
     * would perform some spacing calculation according to span size and index.
     * @param parent Recycle view object.
     * @param childCount Total item counts.
     * @param childIndex Current item index.
     * @param itemSpanSize Total item span size.
     * @param spanIndex current item span index.
     *
     * @return calculated Top edge bound.
     */
    private fun isTopEdge(
        parent: RecyclerView, childCount: Int,
        childIndex: Int, itemSpanSize: Int, spanIndex: Int
    ): Boolean {
        return if (orientation == VERTICAL) {
            childIndex == 0 || isFirstItemEdgeValid(childIndex < spanCount, parent, childIndex)
        } else {
            spanIndex == 0
        }
    }

    /***
     * Validate if edge is bottom bound or not. If it is bottom bound edge we
     * would perform some spacing calculation according to span size and index.
     * @param parent Recycle view object.
     * @param childCount Total item counts.
     * @param childIndex Current item index.
     * @param itemSpanSize Total item span size.
     * @param spanIndex current item span index.
     *
     * @return calculated bottom edge bound.
     */
    private fun isBottomEdge(
        parent: RecyclerView, childCount: Int,
        childIndex: Int, itemSpanSize: Int, spanIndex: Int
    ): Boolean {
        return if (orientation == VERTICAL) {
            isLastItemEdgeValid(
                childIndex >= childCount - spanCount,
                parent, childCount, childIndex, spanIndex
            )
        } else {
            spanIndex + itemSpanSize == spanCount
        }
    }

    /**
     * Validate if first visible item edge bound is valid.
     *
     * @param isOneOfFirstItems should check for first item bound
     * @param parent            Recycle view object
     * @param childIndex        current item index.
     * @return True if first item edge is correct rect bounds. else return false.
     */
    private fun isFirstItemEdgeValid(
        isOneOfFirstItems: Boolean,
        parent: RecyclerView, childIndex: Int
    ): Boolean {
        var totalSpanArea = 0
        if (isOneOfFirstItems) {
            for (i in childIndex downTo 0) {
                totalSpanArea += getItemSpanSize(parent, i)
            }
        }
        return isOneOfFirstItems && totalSpanArea <= spanCount
    }

    /**
     * Validate if last visible item edge bound is valid.
     *
     * @param isOneOfLastItems should check for last item bound.
     * @param parent           Recycle view object.
     * @param childCount       total item count.
     * @param childIndex       current item index.
     * @param spanIndex        current item span count.
     * @return True if last item edge is correct rect bounds. else return false.
     */
    private fun isLastItemEdgeValid(
        isOneOfLastItems: Boolean, parent: RecyclerView,
        childCount: Int, childIndex: Int, spanIndex: Int
    ): Boolean {
        var totalSpanRemaining = 0
        if (isOneOfLastItems) {
            for (i in childIndex until childCount) {
                //totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i)
                totalSpanRemaining += getItemSpanSize(parent, i)
            }
        }
        return isOneOfLastItems && totalSpanRemaining <= spanCount - spanIndex
    }

    companion object {

        private const val VERTICAL = OrientationHelper.VERTICAL
    }

    //endregion
}
