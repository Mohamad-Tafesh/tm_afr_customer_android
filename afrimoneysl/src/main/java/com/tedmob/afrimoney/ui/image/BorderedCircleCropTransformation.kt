package com.tedmob.afrimoney.ui.image

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.OriginalSize
import coil.size.PixelSize
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

class BorderedCircleCropTransformation(
    @Px private val strokeWidthInPx: Int,
    @ColorInt private val strokeColor: Int,
    private val scale: Scale = Scale.FILL,
    @ColorInt private val backgroundColor: Int? = null,
) : Transformation {

    override fun key(): String = "${BorderedCircleCropTransformation::class.java.name}-$strokeWidthInPx,$strokeColor"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val (width, height) = when (size) {
            OriginalSize -> input.width to input.height
            is PixelSize -> size.width to size.height
        }

        val minSize = min(width, height)
        val radius = minSize / 2f
        val output = pool.get(minSize, minSize, input.config ?: Bitmap.Config.ARGB_8888)
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = XFERMODE

            when (scale) {
                Scale.FILL -> {
                    val rect = RectF(
                        radius - width / 2f,
                        radius - height / 2f,
                        radius + width / 2f,
                        radius + height / 2f,
                    )
                    drawBitmap(input, null, rect, paint)
                }

                Scale.FIT -> {
                    if (backgroundColor != null) {
                        drawColor(backgroundColor)
                    }

                    val rect = RectF(
                        radius - input.width / 2f,
                        radius - input.height / 2f,
                        radius + input.width / 2f,
                        radius + input.height / 2f,
                    )
                    drawBitmap(input, null, rect, paint)
                }
            }

            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                color = strokeColor
                strokeWidth = strokeWidthInPx.toFloat()
            }.let { drawCircle(radius, radius, radius - strokeWidthInPx / 2f, it) }
        }

        return output
    }

    override fun equals(other: Any?) = other is BorderedCircleCropTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() =
        "BorderedCircleCropTransformation(strokeWidthInPx=$strokeWidthInPx,strokeColor=$strokeColor)"

    private companion object {
        val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
}
