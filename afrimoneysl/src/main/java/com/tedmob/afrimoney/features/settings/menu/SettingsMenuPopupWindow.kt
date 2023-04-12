package com.tedmob.afrimoney.features.settings.menu

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tedmob.afrimoney.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

open class SettingsMenuPopupWindow
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.popupStyle,
    defStyleRes: Int = R.style.SettingsMenuPopupWindow
) : PopupWindow(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val POPUP_MENU = 0
        const val DIALOG = 1

        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }


    protected val elevation = IntArray(2)
    protected val margin = Array(2) { IntArray(2) }
    internal val listPadding = Array(2) { IntArray(2) }
    protected var itemHeight = 0
    protected var dialogMaxWidth = 0
    protected var unit = 0
    protected var maxUnits = 0

    var mode = POPUP_MENU
        set(value) {
            field = value
            adapter.popupMode = value
        }

    private var requestMeasure = true

    private val list: RecyclerView
    private val adapter: SettingsMenuListAdapter

    var onItemClickListener: ((i: Int) -> Unit)? = null
        set(value) {
            field = value
            adapter.onItemClickListener = value
        }
    var entries: List<CharSequence> = emptyList()
        set(value) {
            field = value
            adapter.entries = value
        }
    var selectedIndex = 0
        set(value) {
            field = value
            adapter.itemSelectedIndex = value
        }

    private var measuredWidth = 0


    init {
        isFocusable = true
        isOutsideTouchable = false

        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.SettingsMenuPopupWindow,
            defStyleAttr,
            defStyleRes
        )

        elevation[POPUP_MENU] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_listElevation, 4f).toInt()
        elevation[DIALOG] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_dialogElevation, 48f).toInt()
        margin[POPUP_MENU][HORIZONTAL] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_listMarginHorizontal, 0f).toInt()
        margin[POPUP_MENU][VERTICAL] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_listMarginVertical, 0f).toInt()
        margin[DIALOG][HORIZONTAL] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_dialogMarginHorizontal, 0f).toInt()
        margin[DIALOG][VERTICAL] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_dialogMarginVertical, 0f).toInt()
        listPadding[POPUP_MENU][HORIZONTAL] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_listItemPadding, 0f).toInt()
        listPadding[DIALOG][HORIZONTAL] =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_dialogItemPadding, 0f).toInt()

        dialogMaxWidth =
            a.getDimension(R.styleable.SettingsMenuPopupWindow_dialogMaxWidth, 0f).toInt()

        unit = a.getDimension(R.styleable.SettingsMenuPopupWindow_unit, 0f).toInt()

        maxUnits = a.getInteger(R.styleable.SettingsMenuPopupWindow_maxUnits, 0)

        a.recycle()

        list = (LayoutInflater.from(context).inflate(
            R.layout.settings_menu_list,
            null
        ) as RecyclerView)
            .apply {
                isFocusable = true
                layoutManager = LinearLayoutManager(context)
                itemAnimator = null
            }
        contentView = list

        adapter = SettingsMenuListAdapter(/*this*/)
            .apply {
                entries = this@SettingsMenuPopupWindow.entries
                itemSelectedIndex = selectedIndex
                onItemClickListener = this@SettingsMenuPopupWindow.onItemClickListener
                popupMode = mode
                horizontalPadding = listPadding[popupMode][HORIZONTAL]
            }
        list.adapter = adapter

        // TODO do not hardcode
        itemHeight = (context.resources.displayMetrics.density * 48).roundToInt()

        val verticalPadding = (context.resources.displayMetrics.density * 8).roundToInt()
        listPadding[POPUP_MENU][VERTICAL] = verticalPadding
        listPadding[DIALOG][VERTICAL] = verticalPadding
    }


    override fun getContentView(): RecyclerView = super.getContentView() as RecyclerView

    override fun getBackground(): FixedBoundsDrawable {
        val background = super.getBackground()
        if (background != null && background !is FixedBoundsDrawable) {
            setBackgroundDrawable(background)
        }

        return super.getBackground() as FixedBoundsDrawable
    }

    override fun setBackgroundDrawable(background: Drawable) {
        if (background !is FixedBoundsDrawable) {
            super.setBackgroundDrawable(
                FixedBoundsDrawable(
                    background
                )
            )
        } else {
            super.setBackgroundDrawable(background)
        }
    }

    /**
     * Show the PopupWindow
     *
     * @param anchor View that will be used to calc the position of windows
     * @param container View that will be used to calc the position of windows
     * @param extraMargin extra margin start
     */
    open fun show(anchor: View, container: View, extraMargin: Int) {
        val maxMaxWidth = container.width - margin[POPUP_MENU][HORIZONTAL] * 2
        val measuredWidth: Int = measureWidth(maxMaxWidth, entries)
        if (measuredWidth == -1) {
            mode = DIALOG
        } else if (measuredWidth != 0) {
            mode = POPUP_MENU
            this.measuredWidth = measuredWidth
        }

        adapter.notifyDataSetChanged()

        if (mode == POPUP_MENU) {
            showPopupMenu(anchor, container, measuredWidth, extraMargin)
        } else {
            showDialog(anchor, container)
        }
    }

    /**
     * Show popup window in dialog mode
     *
     * @param parent a parent view to get the [android.view.View.getWindowToken] token from
     * @param container Container view that holds preference list, also used to calc width
     */
    private fun showDialog(parent: View, container: View) {
        val index = max(0, selectedIndex)
        val count: Int = entries.size

        contentView.overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
        contentView.scrollToPosition(index)

        val width = min(dialogMaxWidth, container.width - margin[DIALOG][HORIZONTAL] * 2)
        setWidth(width)
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        animationStyle = R.style.Animation_SettingsMenuCenter
        setElevation(elevation[DIALOG].toFloat())

        super.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0)

        contentView.post {
            val width = contentView.width
            val height = contentView.height
            val start = Rect(width / 2, height / 2, width / 2, height / 2)

            contentView.startEnterAnimation(
                background,
                width,
                height,
                width / 2,
                height / 2,
                start,
                itemHeight,
                elevation[DIALOG] / 4,
                index
            )
        }

        contentView.post {
            // disable over scroll when no scroll
            val lm = contentView.layoutManager as LinearLayoutManager?
            if (
                lm?.findFirstCompletelyVisibleItemPosition() == 0 &&
                lm.findLastCompletelyVisibleItemPosition() == count - 1
            ) {
                contentView.overScrollMode = View.OVER_SCROLL_NEVER
            }
        }
    }

    /**
     * Show popup window in popup mode
     *
     * @param anchor View that will be used to calc the position of the window
     * @param container Container view that holds preference list, also used to calc width
     * @param width Measured width of this window
     */
    private fun showPopupMenu(anchor: View, container: View, width: Int, extraMargin: Int) {
        val rtl = container.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

        val index = max(0, selectedIndex)
        val count: Int = entries.size

        val anchorTop = anchor.top
        val anchorHeight = anchor.height
        val measuredHeight = itemHeight * count + listPadding[POPUP_MENU][VERTICAL] * 2
        val location = IntArray(2)
        container.getLocationInWindow(location)
        val containerTopInWindow = location[1]
        val containerHeight = container.height

        var y: Int
        var height = measuredHeight
        val elevation = elevation[POPUP_MENU]
        val centerX = if (rtl)
            location[0] + extraMargin - width + listPadding[POPUP_MENU][HORIZONTAL]
        else
            location[0] + extraMargin + listPadding[POPUP_MENU][HORIZONTAL]
        val centerY: Int
        val animItemHeight = itemHeight + listPadding[POPUP_MENU][VERTICAL] * 2
        var animIndex = index
        val animStartRect: Rect

        if (height > containerHeight) { // too high, use scroll

            y = containerTopInWindow + margin[POPUP_MENU][VERTICAL]
            // scroll to select item
            val scroll = (itemHeight * index - anchorTop) +
                    listPadding[POPUP_MENU][VERTICAL] + margin[POPUP_MENU][VERTICAL] - anchorHeight / 2 + itemHeight / 2

            contentView.post {
                contentView.scrollBy(0, -measuredHeight) // to top
                contentView.scrollBy(0, scroll)
            }
            contentView.overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
            height = containerHeight - margin[POPUP_MENU][VERTICAL] * 2
            animIndex = index
            centerY = itemHeight * index

        } else { // calc align to selected

            y = containerTopInWindow + anchorTop + anchorHeight / 2 - itemHeight / 2 -
                    listPadding[POPUP_MENU][VERTICAL] - index * itemHeight
            // make sure window is in parent view
            val maxY = containerTopInWindow + containerHeight -
                    measuredHeight - margin[POPUP_MENU][VERTICAL]
            y = min(y, maxY)
            val minY = containerTopInWindow + margin[POPUP_MENU][VERTICAL]
            y = max(y, minY)
            contentView.overScrollMode = View.OVER_SCROLL_NEVER
            // center of selected item
            centerY =
                (listPadding[POPUP_MENU][VERTICAL] + index * itemHeight + itemHeight * 0.5).toInt()

        }

        setWidth(width)
        setHeight(height)
        setElevation(elevation.toFloat())
        animationStyle = R.style.Animation_SettingsMenuCenter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            enterTransition = null
            exitTransition = null
        }

        super.showAtLocation(anchor, Gravity.NO_GRAVITY, centerX, y)

        val startTop = centerY - (itemHeight * 0.2).toInt()
        val startBottom = centerY + (itemHeight * 0.2).toInt()
        val startLeft: Int
        val startRight: Int
        if (!rtl) {
            startLeft = centerX
            startRight = centerX + unit
        } else {
            startLeft = centerX + width - unit
            startRight = centerX + width
        }
        animStartRect = Rect(startLeft, startTop, startRight, startBottom)
        val animElevation = (elevation * 0.25).roundToInt()

        postStartEnterAnimation(
            background,
            width,
            height,
            centerX,
            centerY,
            animStartRect,
            animItemHeight,
            animElevation,
            animIndex
        )
    }

    /**
     * Request a measurement before next show, call this when entries changed.
     */
    open fun requestMeasure() {
        requestMeasure = true
    }

    /**
     * Measure window width
     *
     * @param maxWidth max width for popup
     * @param entries Entries of preference hold this window
     * @return  0: skip
     * -1: use dialog
     * other: measuredWidth
     */
    private fun measureWidth(maxWidth: Int, entries: List<CharSequence>): Int {
        // skip if should not measure
        if (!requestMeasure) {
            return 0
        }

        var maxWidth = maxWidth
        val sortedEntriesBySize = entries.sortedBy { it.length }

        requestMeasure = false

        val context = contentView.context
        var width = 0
        maxWidth = min(unit * maxUnits, maxWidth)
        val bounds = Rect()
        val textPaint: Paint = TextPaint()

        // TODO do not hardcode
        textPaint.textSize = 16 * context.resources.displayMetrics.scaledDensity
        for (chs in sortedEntriesBySize) {
            //fixme is toString() needed here?
            textPaint.getTextBounds(chs.toString(), 0, chs.length, bounds)
            width = max(
                width,
                bounds.width() + listPadding[POPUP_MENU][HORIZONTAL] * 2
            )
            // more than one line should use dialog
            if (width > maxWidth || chs.toString().contains("\n")) {
                return -1
            }
        }

        // width is a multiple of a unit
        var w = 0
        while (width > w) {
            w += unit
        }

        return w
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        throw UnsupportedOperationException("use show(anchor) to show the window")
    }

    override fun showAsDropDown(anchor: View?) {
        throw UnsupportedOperationException("use show(anchor) to show the window")
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int) {
        throw UnsupportedOperationException("use show(anchor) to show the window")
    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        throw UnsupportedOperationException("use show(anchor) to show the window")
    }
}