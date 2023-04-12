package com.tedmob.afrimoney.ui.drag

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import com.tedmob.afrimoney.R


class DraggableConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    interface ViewDragListener {
        fun onViewCaptured(view: View, i: Int)
        fun onViewReleased(view: View, v: Float, v1: Float)
    }


    private val viewDragHelper: ViewDragHelper
    var viewDragListener: ViewDragListener? = null


    class LayoutParams : ConstraintLayout.LayoutParams {

        enum class Range {
            FREE,
            VERTICAL,
            HORIZONTAL
        }


        var isDraggable: Boolean = false
        var dragBounds: DragBoundsDelimiter = DragBoundsDelimiter.Custom(Range.FREE)


        constructor(width: Int, height: Int) : super(width, height)

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            context.withStyledAttributes(attrs, R.styleable.DraggableConstraintLayout_LayoutParams) {
                isDraggable = getBoolean(R.styleable.DraggableConstraintLayout_LayoutParams_isDraggable, false)

                val dragRange = getInt(R.styleable.DraggableConstraintLayout_LayoutParams_dragRange, Range.FREE.ordinal)
                    .let { ordinal ->
                        Range.values()
                            .firstOrNull { it.ordinal == ordinal }
                            ?: Range.FREE
                    }
                dragBounds = getInt(R.styleable.DraggableConstraintLayout_LayoutParams_dragBounds, 0)
                    .let { value ->
                        when (value) {
                            1 -> DragBoundsDelimiter.Unbounded(dragRange)
                            2 -> DragBoundsDelimiter.InsideParent(dragRange)
                            3 -> {
                                val otherViewId =
                                    getResourceId(R.styleable.DraggableConstraintLayout_LayoutParams_dragBoundsView, 0)
                                        .takeUnless { it == 0 }
                                DragBoundsDelimiter.InsideOtherView(dragRange, otherViewId)
                            }
                            else -> DragBoundsDelimiter.Custom(dragRange)
                        }
                    }
            }
        }

        constructor(p: LayoutParams) : super(p)
        constructor(p: ConstraintLayout.LayoutParams) : super(p)
        constructor(p: MarginLayoutParams) : super(p)
        constructor(p: ViewGroup.LayoutParams) : super(p)
    }

    sealed class DragBoundsDelimiter(val direction: LayoutParams.Range) {

        protected inline val isVerticalAllowed: Boolean
            get() = direction == LayoutParams.Range.VERTICAL || direction == LayoutParams.Range.FREE
        protected inline val isHorizontalAllowed: Boolean
            get() = direction == LayoutParams.Range.HORIZONTAL || direction == LayoutParams.Range.FREE

        protected inline fun Int.onlyIfVerticalAllowed(): Int = if (isVerticalAllowed) this else 0
        protected inline fun Int.onlyIfHorizontalAllowed(): Int = if (isHorizontalAllowed) this else 0


        class Custom(direction: LayoutParams.Range = LayoutParams.Range.FREE) : DragBoundsDelimiter(direction) {

            var onGetHorizontalDragRange: (magnitude: Int, view: View, parent: DraggableConstraintLayout) -> Int =
                { magnitude, _, _ -> magnitude }

            var onGetVerticalDragRange: (magnitude: Int, view: View, parent: DraggableConstraintLayout) -> Int =
                { magnitude, _, _ -> magnitude }

            var onClampPositionHorizontal: (
                view: View,
                left: Int,
                dx: Int,
                parent: DraggableConstraintLayout
            ) -> Int = { _, left, _, _ -> left }

            var onClampPositionVertical: (
                view: View,
                top: Int,
                dy: Int,
                parent: DraggableConstraintLayout
            ) -> Int = { _, top, _, _ -> top }


            override fun getViewHorizontalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                if (isHorizontalAllowed) {
                    onGetHorizontalDragRange(view.width, view, parent)
                } else {
                    0
                }

            override fun getViewVerticalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                if (isVerticalAllowed) {
                    onGetVerticalDragRange(view.height, view, parent)
                } else {
                    0
                }

            override fun clampViewPositionHorizontal(
                view: View,
                left: Int,
                dx: Int,
                parent: DraggableConstraintLayout
            ): Int = if (isHorizontalAllowed) {
                onClampPositionHorizontal(view, left, dx, parent)
            } else {
                0
            }

            override fun clampViewPositionVertical(
                view: View,
                top: Int,
                dy: Int,
                parent: DraggableConstraintLayout
            ): Int = if (isVerticalAllowed) {
                onClampPositionVertical(view, top, dy, parent)
            } else {
                0
            }
        }

        class Unbounded(direction: LayoutParams.Range) : DragBoundsDelimiter(direction) {

            override fun getViewHorizontalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                view.width.onlyIfHorizontalAllowed()

            override fun getViewVerticalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                view.height.onlyIfVerticalAllowed()

            override fun clampViewPositionHorizontal(
                view: View,
                left: Int,
                dx: Int,
                parent: DraggableConstraintLayout
            ): Int = left.onlyIfHorizontalAllowed()

            override fun clampViewPositionVertical(
                view: View,
                top: Int,
                dy: Int,
                parent: DraggableConstraintLayout
            ): Int = top.onlyIfVerticalAllowed()
        }

        class InsideParent(direction: LayoutParams.Range) : DragBoundsDelimiter(direction) {

            override fun getViewHorizontalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                parent.width.onlyIfHorizontalAllowed()

            override fun getViewVerticalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                parent.height.onlyIfVerticalAllowed()

            override fun clampViewPositionHorizontal(
                view: View,
                left: Int,
                dx: Int,
                parent: DraggableConstraintLayout
            ): Int {
                val parentLeft = parent.left + parent.paddingLeft
                val parentRight = parent.right - parent.paddingRight

                return left
                    .coerceAtLeast(parentLeft)
                    .coerceAtMost(parentRight - view.width)
            }

            override fun clampViewPositionVertical(
                view: View,
                top: Int,
                dy: Int,
                parent: DraggableConstraintLayout
            ): Int {
                val parentTop = parent.top + parent.paddingTop
                val parentBottom = parent.bottom - parent.paddingBottom

                return top
                    .coerceAtLeast(parentTop)
                    .coerceAtMost(parentBottom - view.height)
            }
        }

        class InsideOtherView(direction: LayoutParams.Range, private val otherViewId: Int?) :
            DragBoundsDelimiter(direction) {

            private inline fun View.getOtherView(id: Int?): View = id?.let { findViewById(it) } ?: this


            override fun getViewHorizontalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                parent.getOtherView(otherViewId).width.onlyIfHorizontalAllowed()

            override fun getViewVerticalDragRange(view: View, parent: DraggableConstraintLayout): Int =
                parent.getOtherView(otherViewId).height.onlyIfVerticalAllowed()

            override fun clampViewPositionHorizontal(
                view: View,
                left: Int,
                dx: Int,
                parent: DraggableConstraintLayout
            ): Int {
                val otherView = parent.getOtherView(otherViewId)
                val parentLeft = otherView.left + otherView.paddingLeft
                val parentRight = otherView.right - otherView.paddingRight

                return left
                    .coerceAtLeast(parentLeft)
                    .coerceAtMost(parentRight - view.width)
            }

            override fun clampViewPositionVertical(
                view: View,
                top: Int,
                dy: Int,
                parent: DraggableConstraintLayout
            ): Int {
                val otherView = parent.getOtherView(otherViewId)
                val parentTop = otherView.top + otherView.paddingTop
                val parentBottom = otherView.bottom - otherView.paddingBottom

                return top
                    .coerceAtLeast(parentTop)
                    .coerceAtMost(parentBottom - view.height)
            }
        }

        abstract fun getViewHorizontalDragRange(view: View, parent: DraggableConstraintLayout): Int
        abstract fun getViewVerticalDragRange(view: View, parent: DraggableConstraintLayout): Int
        abstract fun clampViewPositionHorizontal(view: View, left: Int, dx: Int, parent: DraggableConstraintLayout): Int
        abstract fun clampViewPositionVertical(view: View, top: Int, dy: Int, parent: DraggableConstraintLayout): Int
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams = LayoutParams(context, attrs)

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams = LayoutParams(p)

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams && super.checkLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        children.forEach { view ->
            view.layoutParams?.let { params ->
                if (params is LayoutParams && params.isDraggable) {
                    view.isFocusable = true
                    view.isClickable = true
                }
            }
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(ev)
        return super.onTouchEvent(ev)
    }

    private val dragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(view: View, i: Int): Boolean {
            return view.visibility == VISIBLE && viewIsDraggableChild(view)
        }

        override fun onViewCaptured(view: View, i: Int) {
            viewDragListener?.onViewCaptured(view, i)
        }

        override fun onViewReleased(view: View, v: Float, v1: Float) {
            viewDragListener?.onViewReleased(view, v, v1)
        }

        override fun getViewHorizontalDragRange(view: View): Int =
            (view.layoutParams as? LayoutParams?)?.dragBounds
                ?.getViewHorizontalDragRange(view, this@DraggableConstraintLayout)
                ?: view.width

        override fun getViewVerticalDragRange(view: View): Int =
            (view.layoutParams as? LayoutParams?)?.dragBounds
                ?.getViewVerticalDragRange(view, this@DraggableConstraintLayout)
                ?: view.height

        override fun clampViewPositionHorizontal(view: View, left: Int, dx: Int): Int =
            (view.layoutParams as? LayoutParams?)?.dragBounds
                ?.clampViewPositionHorizontal(view, left, dx, this@DraggableConstraintLayout)
                ?: left

        override fun clampViewPositionVertical(view: View, top: Int, dy: Int): Int =
            (view.layoutParams as? LayoutParams?)?.dragBounds
                ?.clampViewPositionVertical(view, top, dy, this@DraggableConstraintLayout)
                ?: top
    }


    init {
        viewDragHelper = ViewDragHelper.create(this, dragCallback)
    }


    private inline fun viewIsDraggableChild(view: View): Boolean =
        (view.layoutParams as? LayoutParams)?.isDraggable == true
}