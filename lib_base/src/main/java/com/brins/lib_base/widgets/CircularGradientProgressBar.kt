package com.brins.lib_base.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.brins.lib_base.R


class CircularGradientProgressBar(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paint: Paint
    private var rectF: RectF
    private var startAngle = 0
    private val sweepAngle = 120 // 设置圆环显示的弧度长度
    private val rotationSpeed = 10 // 设置旋转速度

    init {
        paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 20f // 圆环的宽度
        }

        rectF = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = Math.min(width, height)
        val pad = paint.strokeWidth / 2 + 10 // 确保圆环在视图内部
        rectF[pad, pad, size - pad] = size - pad

        // 设置渐变色
        paint.setColor(ContextCompat.getColor(context, R.color.color_178CE9))
        /*paint.setShader(
            SweepGradient(
                (size / 2).toFloat(),
                (size / 2).toFloat(),
                intArrayOf(ContextCompat.getColor(context, R.color.color_2980b9), ContextCompat.getColor(context, R.color.color_2980b9), ContextCompat.getColor(context, R.color.white)),
                null
            )
        )*/

        // 绘制圆环
        canvas.drawArc(rectF, startAngle.toFloat(), sweepAngle.toFloat(), false, paint)

        // 更新开始角度
        startAngle += rotationSpeed
        if (startAngle > 360) {
            startAngle -= 360
        }

        // 持续重绘以创建旋转效果
        invalidate()
    }
}



