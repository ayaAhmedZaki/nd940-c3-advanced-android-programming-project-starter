package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
    }

    private val boxBackgroundColor = resources.getColor(R.color.colorPrimary)
    private val textColor = Color.WHITE
    private val fontSize = convertDpToPixel(30f)
    private val text = "Download"

    private lateinit var boxBackground: RectF
    private lateinit var boxBackgroundPaint: Paint
    private var textWidth = 0f
    private var textSmallGlyphHeight = 0f
    private lateinit var textPaint: Paint

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        boxBackground = RectF(0f, 0f, w.toFloat(), h.toFloat())
        boxBackgroundPaint = Paint().apply { color = boxBackgroundColor }
        textPaint = Paint().apply {
            color = textColor
            textSize = fontSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textWidth = measureText(text)
            textSmallGlyphHeight = fontMetrics.run { ascent + descent }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(boxBackground, boxBackgroundPaint)
        val textStartPadding = (width - textWidth) / 2f
        val textTopPadding = (height - textSmallGlyphHeight) / 2f
        canvas?.drawText(text, textStartPadding, textTopPadding, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun convertDpToPixel(dp: Float) =
        dp * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

}