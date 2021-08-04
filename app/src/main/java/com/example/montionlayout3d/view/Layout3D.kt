package com.example.montionlayout3d.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Scroller
import androidx.core.view.ViewConfigurationCompat
import com.example.montionlayout3d.R
import kotlin.math.abs


/**
 * @ClassName Layout3D
 * @Description TODO
 * @Author AlexLu_1406496344@qq.com
 * @Date 2021/7/30 9:29
 */
class Layout3D : FrameLayout {

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

    val Int.sp get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            context.resources.displayMetrics
    )
    val Float.dp get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            context.resources.displayMetrics
    )
    val Int.dp get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
    )

    companion object{
        const val TAG = "Layout3D"

        private const val showLog = true

        fun log(msg: String){
            if (showLog) Log.d(TAG, msg)
        }

    }


    private lateinit var scroller:Scroller
    //布局中心点
    private val centerPoint:PointF = PointF(0f, 0f)
    //parent view宽高
    private var viewHeight:Int = 0
    private var viewWidth:Int = 0

    //上-中-下 层资源
    private var topDrawable:Drawable ?= null
    private var middleDrawable:Drawable ?= null
    private var bottomDrawable:Drawable ?= null

    //是否开启上中下层移动
    private var topSlidingEnable:Boolean = true
    private var middleSlidingEnable:Boolean = true
    private var bottomSlidingEnable:Boolean = true

    //上中下层最大移动距离
    private var topDistance:Float = 60.dp
    private var middleDistance:Float = 40.dp
    private var bottomDistance:Float = 20.dp

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


//    var topImageView :ImageView ?= null
//    var middleImageView :ImageView ?= null
//    var bottomImageView :ImageView ?= null

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


        topDistance = mTypedArray.getDimension(R.styleable.layout3d_TopSlide, topDistance)
        middleDistance = mTypedArray.getDimension(R.styleable.layout3d_MiddleSlide, middleDistance)
        bottomDistance = mTypedArray.getDimension(R.styleable.layout3d_BottomSlide, bottomDistance)
        log("topDistance:${topDistance},middleDistance:${middleDistance},bottomDistance:${bottomDistance}")

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

//        topLayerHeight = mTypedArray.getDimension(R.styleable.layout3d_TopLayerHeight, 0f).toInt()
//        topLayerWidth = mTypedArray.getDimension(R.styleable.layout3d_TopLayerWidth, 0f).toInt()
//        middleLayerHeight = mTypedArray.getDimension(R.styleable.layout3d_MiddleLayerHeight, 0f).toInt()
//        middleLayerWidth = mTypedArray.getDimension(R.styleable.layout3d_MiddleLayerWidth, 0f).toInt()
//        log("drawable xy,top:$topLayerWidth * $topLayerHeight,middle:$middleLayerWidth * $middleLayerHeight")

        mTypedArray.recycle() //回收，回收之后属性集attay不可用

        topDrawable?.let {
            log("add top image")
            addView(createImageView(it))
        }

//        topImageView = topDrawable?.let {
//            createImageView(it)
//        }
//        middleImageView = middleDrawable?.let {
//            createImageView(it)
//        }
//        bottomImageView = bottomDrawable?.let {
//            createImageView(it)
//        }
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas==null) return
        //log("onDraw")

/*

        bottomDrawable?.let {
            //设置位图左上角坐标（前两个参数）和绘制在View上的位图的宽度和高度（后两个参数）
            it.setBounds(0, 0, viewWidth, viewHeight)
            it.draw(canvas)
        }

        middleDrawable?.let {
            it.setBounds(middleLeft, middleTop, middleRight, middleBottom)
            it.draw(canvas)
        }

        topDrawable?.let {
            //it.intrinsicWidth,it.intrinsicHeight代表图片像素 3278,3288
            //log("intrinsicWidth:${it.intrinsicWidth},${it.intrinsicHeight}")
            it.setBounds(topLeft, topTop, topRight, topBottom)
            it.draw(canvas)
        }
        scaleX = 1.3f
        scaleY = 1.3f
*/




//        topImageView?.let {
//            log("add top image")
//            addView(it)
//        }
/*        middleImageView?.let {
            log("add middle image")
            addView(it)
        }
        bottomImageView?.let {
            log("add bottom image")
            addView(it)
        }*/


        log("onDraw,x:${this.x},y:${this.y},scrollX:${this.scrollX},scrollY:${this.scrollY}")
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        //initSensor()

        val height = height
        val width = width

        val measureHeight = measuredHeight
        val measureWidth = measuredWidth

        viewHeight = measureHeight
        viewWidth = measureWidth

        centerPoint.x = (measureWidth/2).toFloat()
        centerPoint.y = (measureHeight/2).toFloat()


        log("height${height},width$width,measureHeight${measureHeight},measureWidth$measureWidth")
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

    @Synchronized
    private fun createImageView(drawable: Drawable):ImageView{
        //val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //val layout = inflater.inflate(R.layout.item, null)
//        val imageView: ImageView = layout.findViewById<ImageView>(R.id.image_3d)
       // val imageView = inflater.inflate(R.layout.item, null) as ImageView


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


    var lastX = 0
    var lastY = 0
    var lastZ = 0

    private fun calculateScroll(x: Int, y: Int, z: Int) {
        log("last x:$lastX , y:$lastY ,z:$lastZ")
        var scrollY = 0
        var scrollX = 0

        var addY = 0

        //除去一下特殊的角度
        //if (abs(x)==180 || abs(x)==90 || abs(y)==180 || abs(y)==90) return
        //if (x !in -90 until 45) return

        if (abs(z) > 90){
            if (y in -180 until 0){
                //从右向左旋转
                //x值增加
                scrollX = abs(((y / 180.0) * topDistance).toInt())
            }else if (y in 0 until 180){
                //从左向右旋转
                scrollX = -abs(((y / 180.0) * topDistance).toInt())
            }
        }



        //X轴逆时针方向旋转，x值变化为0==90==0==-90==0
        //对应Z轴变化 100==0==-80==-130==100
/*        if (z >= 0) {
            //左半区
            if (x >= lastX){
                //逆时针方向
                addY = -abs((((x-lastX)/180.0)*topDistance).toInt())
            }else{
                addY = abs((((x-lastX)/180.0)*topDistance).toInt())
            }
        }else{
            //右边区
            if (x < lastX){
                //逆时针方向
                addY = abs((((x-lastX)/180.0)*topDistance).toInt())
            }else{
                addY = -abs((((x-lastX)/180.0)*topDistance).toInt())
            }
        }*/

/*        if (x in -90 until 90){
            if (x > lastX){
                //逆时针
                //scrollY = abs((((x)/90.0)*topDistance).toInt())
                addY += 3
            }else if (x < lastX){
                //顺时针
                //scrollY = -abs((((x)/90.0)*topDistance).toInt())
                addY -= 3
            }else{
                addY = 0
            }
        }*/

        if (x in -90 until 0){
            scrollY = abs((((x) / 180.0) * bottomDistance).toInt())
        }else if (x in 0 until 90){
            scrollY = -abs((((x) / 180.0) * bottomDistance).toInt())
        }

        lastX = x
        lastY = y
        lastZ = z

        if(abs(x) < 5) this@Layout3D.scrollY = 0
        if(abs(y) < 5) this@Layout3D.scrollX = 0


        //this@Layout3D.scrollY += addY
        this@Layout3D.scrollX = scrollX
        this@Layout3D.scrollY = scrollY

        log("scrollY:${this@Layout3D.scrollY}")
        //log("onSensorChanged,scrollX:${scrollX},scrollY:${scrollY},x:${x} y:${y}")
    }

}