package com.example.montionlayout3d.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewConfigurationCompat
import com.example.montionlayout3d.R
import kotlin.math.abs

val Int.sp get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)
val Int.dp get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)
val Float.dp get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics
)
val Float.sp get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this,
        Resources.getSystem().displayMetrics
)


class Layout3D : ViewGroup{

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }


    companion object{
        const val TAG = "Layout3D"

        private const val showLog = true

        fun log(msg: String){
            if (showLog) Log.d(TAG, msg)
        }

    }


    private lateinit var scroller:Scroller

    //上-中-下 层资源
    private var topDrawable:Drawable ?= null
    private var middleDrawable:Drawable ?= null
    private var bottomDrawable:Drawable ?= null

    //是否开启上中下层移动
    private var topSlidingEnable:Boolean = true
    private var middleSlidingEnable:Boolean = true
    private var bottomSlidingEnable:Boolean = true

    //最大移动距离
    private var slideDistance:Float = 60.dp

    //中层图片坐标
    private var middleTop = 0
    private var middleBottom = 0
    private var middleLeft = 0
    private var middleRight = 0

    //上层图片坐标
    private var topTop = 0
    private var topBottom = 0
    private var topLeft = 0
    private var topRight = 0


    var topImageView :ImageView ?= null
    var middleImageView :ImageView ?= null
    var bottomImageView :ImageView ?= null

    private var topLayerHeight:Int = 0
    private var topLayerWidth:Int = 0
    private var middleLayerHeight:Int = 0
    private var middleLayerWidth:Int = 0


    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?){
        scroller = Scroller(context)
        val configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        val mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration)

        val mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.layout3d)

        topDrawable = mTypedArray.getDrawable(R.styleable.layout3d_TopLayer)
        middleDrawable = mTypedArray.getDrawable(R.styleable.layout3d_MiddleLayer)
        bottomDrawable = mTypedArray.getDrawable(R.styleable.layout3d_BottomLayer)
        log("top:${topDrawable},middle:${middleDrawable},bottom:${bottomDrawable}")


        topSlidingEnable = mTypedArray.getBoolean(R.styleable.layout3d_TopSlidingEnable, true)
        middleSlidingEnable = mTypedArray.getBoolean(R.styleable.layout3d_MiddleSlidingEnable, true)
        bottomSlidingEnable = mTypedArray.getBoolean(R.styleable.layout3d_BottomSlidingEnable, true)
        log("topEnable:${topSlidingEnable},middleEnable:${middleSlidingEnable},bottomEnable:${bottomSlidingEnable}")

        slideDistance = mTypedArray.getDimension(R.styleable.layout3d_SlideDistance, slideDistance)
        log("slideDistance:${slideDistance}")

        middleTop = mTypedArray.getDimension(R.styleable.layout3d_MiddleLayerTop, 0f).toInt()
        middleBottom = mTypedArray.getDimension(
                R.styleable.layout3d_MiddleLayerBottom,
                0f
        ).toInt()
        middleLeft = mTypedArray.getDimension(R.styleable.layout3d_MiddleLayerLeft, 0f).toInt()
        middleRight = mTypedArray.getDimension(
                R.styleable.layout3d_MiddleLayerRight,
                0f
        ).toInt()
        log("middle-margin,top:${middleTop},bottom:$middleBottom,left:${middleLeft},right:${middleRight}")

        topTop = mTypedArray.getDimension(R.styleable.layout3d_TopLayerTop, 0f).toInt()
        topBottom = mTypedArray.getDimension(R.styleable.layout3d_TopLayerBottom, 0f).toInt()
        topLeft = mTypedArray.getDimension(R.styleable.layout3d_TopLayerLeft, 0f).toInt()
        topRight = mTypedArray.getDimension(R.styleable.layout3d_TopLayerRight, 0f).toInt()
        log("top-margin,top:${topTop},bottom:$topBottom,left:${topLeft},right:${topRight}")

        mTypedArray.recycle() //回收，回收之后属性集attay不可用

        bottomDrawable?.let {
            //createImageView(it)
            bottomImageView = SlideImageView(it,context,this)
        }
        middleDrawable?.let {
            //createImageView(it)
            middleImageView = SlideImageView(it,context,this)
        }
        topDrawable?.let {
            //createImageView(it)
            topImageView = SlideImageView(it,context,this)
        }
        initSensor()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        log("onLayout")
        bottomImageView?.layout(0,0,measuredWidth,measuredHeight)
        middleImageView?.layout(middleLeft,middleTop,middleRight,middleBottom)
        topImageView?.layout(topLeft,topTop,topRight,topBottom)
    }



    private var mAcceleValues : FloatArray ?= null
    private var mMageneticValues : FloatArray ?= null
    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                mAcceleValues = event.values
                //log("x:${event.values[0]},y:${event.values[1]},z:${event.values[2]}")
            }
            if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mMageneticValues = event.values
            }
            if (mAcceleValues==null || mMageneticValues==null) return

            val values = FloatArray(3)
            val R = FloatArray(9)
            SensorManager.getRotationMatrix(R, null, mAcceleValues, mMageneticValues);
            SensorManager.getOrientation(R, values);

            //val z = values[0].toDouble()
            // x轴的偏转角度
            //val x = Math.toDegrees(values[1].toDouble()).toFloat()
            // y轴的偏转角度
            //val y = Math.toDegrees(values[2].toDouble()).toFloat()

            val degreeZ = Math.toDegrees(values[0].toDouble()).toInt()
            val degreeX = Math.toDegrees(values[1].toDouble()).toInt()
            val degreeY = Math.toDegrees(values[2].toDouble()).toInt()
            log("x:${degreeX},y:${degreeY},z:${degreeZ}")
            calculateScroll(degreeX, degreeY, degreeZ)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }



    private var hasInit = false
    private fun initSensor(){
        if (hasInit) return
        log("initSensor")
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 重力传感器
        val acceleSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(listener, acceleSensor, SensorManager.SENSOR_DELAY_GAME)

        // 地磁场传感器
        val magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(listener, magneticSensor, SensorManager.SENSOR_DELAY_GAME)

        hasInit = true
    }

    class SlideImageView(drawable: Drawable,context: Context,parent:ViewGroup) : AppCompatImageView(context){
        init {
            background = drawable
            //setBackgroundDrawable(drawable)
            layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                leftMargin = 100
                rightMargin = 100
                topMargin = 100
                bottomMargin = 100
            }
            scaleType = ScaleType.CENTER_CROP
            fitsSystemWindows = true
            isClickable = true
            parent.addView(this)
        }
    }

    @Synchronized
    private fun createImageView(drawable: Drawable):ImageView{
        val imageView = ImageView(context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //val params = ViewGroup.LayoutParams(100.dp.toInt(), 100.dp.toInt())
        imageView.apply {
            layoutParams = params
            scaleType = ImageView.ScaleType.FIT_CENTER
            adjustViewBounds = true
            background = drawable
        }

        log("createImageView: ${imageView.measuredHeight} * ${imageView.measuredWidth}")
        return imageView
    }



    private fun calculateScroll(x: Int, y: Int, z: Int) {
        var deltaY = 0
        var deltaX = 0

        //除去一下特殊的角度
        //if (abs(x)==180 || abs(x)==90 || abs(y)==180 || abs(y)==90) return
        //if (x !in -90 until 45) return

        if (abs(z) > 90){
            if (y in -180 until 0){
                //从右向左旋转
                //x值增加
                deltaX = abs(((y / 180.0) * slideDistance).toInt())
            }else if (y in 0 until 180){
                //从左向右旋转
                deltaX = -abs(((y / 180.0) * slideDistance).toInt())
            }
        }

        if (x in -90 until 0){
            deltaY = abs((((x) / 180.0) * slideDistance).toInt())
        }else if (x in 0 until 90){
            deltaY = -abs((((x) / 180.0) * slideDistance).toInt())
        }

        if (abs(deltaX) < 5) deltaX=0
        if (abs(deltaY) < 5) deltaY=0
        scroller.startScroll(deltaX,deltaY, (this.x+deltaX).toInt(),(this.y+deltaY).toInt(),1000)
        invalidate()
        log("onSensorChanged,scrollX:${deltaX},scrollY:${deltaY},x:${x} y:${y}")
    }


    override fun computeScroll() {
        super.computeScroll()
        //判断Scroller是否执行完毕
        if (scroller.computeScrollOffset()) {
            val slideX = scroller.currX
            val slideY = scroller.currY

            val bottomSlideX = slideX/3
            val bottomSlideY = slideY/3

            val middleSlideX = slideX/2
            val middleSlideY = slideY/2

            val topSlideX = -slideX
            val topSlideY = -slideY

            bottomImageView?.layout(0+bottomSlideX,0+bottomSlideY,measuredWidth+bottomSlideX,measuredHeight+bottomSlideY)
            //middleImageView?.layout(middleLeft+middleSlideX,middleTop+middleSlideY,middleRight+middleSlideX,middleBottom+middleSlideY)
            topImageView?.layout(topLeft+topSlideX,topTop+topSlideY,topRight+topSlideX,topBottom+topSlideY)

            /*(bottomImageView as View).scrollTo(
                scroller.currX,
                scroller.currY
            )*/
            //通过重绘来不断调用computeScroll
            invalidate()
        }
    }

}