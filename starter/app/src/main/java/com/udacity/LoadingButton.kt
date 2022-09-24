package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(

    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var bgColor: Int = 0
    private var textColor: Int = 0

    @Volatile
    private var progress: Double = 0.0
    private var valueAnimator: ValueAnimator

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
    }

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()
        requestLayout()
    }

    fun completedDownload() {
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
        requestLayout()
    }

    init {
        isClickable = true
        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            R.animator.loading_animation
        ) as ValueAnimator

        valueAnimator.addUpdateListener(updateListener)

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            bgColor = getColor(R.styleable.LoadingButton_bgColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER // button text alignment
        textSize = 30.0f //button text size
        typeface = Typeface.create("", Typeface.BOLD) // button text's font style
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        animation()

        return true
    }

    private fun animation() {
        valueAnimator.start()
    }
    private val rect = RectF(
        740f,
        50f,
        810f,
        110f
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = 0f
        paint.color = bgColor
        // draw custom button
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
//        val color = Paint()
//        color.color = Color.parseColor("#F9A825")
//        canvas.drawArc(0f,0f,(width / 2).toFloat(), ((height + 30) / 2).toFloat(), (progress / 100.0).toFloat(), 360f, true, color)

        // to show rectangular progress on custom button while file is downloading
        if (buttonState == ButtonState.Loading) {
            paint.color = Color.parseColor("#004349")
            canvas.drawRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), paint
            )
            paint.color = Color.parseColor("#F9A825")
            canvas.drawArc(10f,10f,(width /8).toFloat(), ((height-10)).toFloat(), 0f, (360 * (progress / 100)).toFloat(), true, paint)

        }
        // check the button state
        val buttonText = if (buttonState == ButtonState.Loading)
            resources.getString(R.string.loading) // We are loading as button text
        else resources.getString(R.string.download)// download as button text

        // write the text on custom button
        paint.color = textColor
        canvas.drawText(buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
    }
}
