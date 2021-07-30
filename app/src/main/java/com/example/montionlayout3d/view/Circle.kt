package com.example.montionlayout3d.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.montionlayout3d.R

/**
 * @ClassName Circle
 * @Description TODO
 * @Author AlexLu_1406496344@qq.com
 * @Date 2021/7/30 10:18
 */
class Circle : View {

    private val mPaint = Paint()
    private var mColor = 0
    private var mAlpha = 0
    private var mRadius = 0f


    val centerPoint = PointF(0f,0f)

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(set: AttributeSet?) {
        val mTypedArray = context.obtainStyledAttributes(set, R.styleable.circle)
        mColor = mTypedArray.getColor(R.styleable.circle_color_c, Color.BLACK)
        mRadius = mTypedArray.getDimension(R.styleable.circle_radius_c, 50f)
        mAlpha = mTypedArray.getInteger(R.styleable.circle_alpha_c, 100);
        mTypedArray.recycle() //回收，回收之后属性集attay不可用

        mPaint.strokeWidth = 10f
        mPaint.color = mColor
        mPaint.alpha = mAlpha;
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerPoint.x, centerPoint.y, mRadius, mPaint)
        canvas.save()
        Log.d("Circle", "${mAlpha},${mRadius},${mColor}")
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val height = height
        val width = width

        val measureHeight = measuredHeight
        val measureWidth = measuredWidth

        centerPoint.x = (measureWidth/2).toFloat()
        centerPoint.y = (measureHeight/2).toFloat()

        //Log.d("wh","height${height},width$width,measureHeight${measureHeight},measureWidth$measureWidth")
    }


}