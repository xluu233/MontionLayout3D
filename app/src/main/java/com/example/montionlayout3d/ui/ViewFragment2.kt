package com.example.montionlayout3d.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.montionlayout3d.MainViewModel
import com.example.montionlayout3d.R
import com.example.montionlayout3d.databinding.FragmentCusviewBinding
import com.example.montionlayout3d.databinding.FragmentMainBinding
import com.example.montionlayout3d.databinding.FragmentVgBinding
import com.example.montionlayout3d.view.Layout3D
import kotlin.math.abs

class ViewFragment2 : Fragment() {


    private lateinit var viewModel: MainViewModel
    private lateinit var binding : FragmentVgBinding


    private var mAcceleValues : FloatArray ?= null
    private var mMageneticValues : FloatArray ?= null

    var lastX = 0
    var lastY = 0
    var lastZ = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVgBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initSensor()
    }

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

            val degreeZ = Math.toDegrees(values[0].toDouble()).toInt()
            val degreeX = Math.toDegrees(values[1].toDouble()).toInt()
            val degreeY = Math.toDegrees(values[2].toDouble()).toInt()
            Layout3D.log("x:${degreeX},y:${degreeY},z:${degreeZ}")
            calculateScroll(degreeX, degreeY, degreeZ)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    private fun initSensor(){
        Layout3D.log("initSensor")
        val sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 重力传感器
        val acceleSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(listener, acceleSensor, SensorManager.SENSOR_DELAY_GAME)

        // 地磁场传感器
        val magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(listener, magneticSensor, SensorManager.SENSOR_DELAY_GAME)

    }





    //不同图层的最大移动距离
    var topDistance = 100
    var middleDistance = 70
    var bottomDistance = 100


    /**
     * TODO 计算位移
     *
     * @param x
     * @param y
     * @param z
     */
    private fun calculateScroll(x: Int, y: Int, z: Int) {
        Layout3D.log("last x:$lastX , y:$lastY ,z:$lastZ")

        var scrollY = 0
        var scrollX = 0

        if (abs(z) > 90){
            if (y in -180 until 0){
                //从右向左旋转
                //x值增加
                scrollX = abs(((y / 180.0) * bottomDistance).toInt())
            }else if (y in 0 until 180){
                //从左向右旋转
                scrollX = -abs(((y / 180.0) * bottomDistance).toInt())
            }
        }

        if (x in -90 until 0){
            scrollY = abs((((x) / 180.0) * bottomDistance /2).toInt())
        }else if (x in 0 until 90){
            scrollY = -abs((((x) / 180.0) * bottomDistance /2).toInt())
        }

        lastX = x
        lastY = y
        lastZ = z

        if(abs(x) < 5) scrollY = 0
        if(abs(y) < 5) scrollX = 0

        binding.back.scrollTo(scrollX,scrollY)

        val topScrollX = scrollX*(topDistance/bottomDistance)
        val topScrollY = scrollY*(topDistance/bottomDistance)
        binding.top.scrollTo(-topScrollX,-topScrollY)

        val midScrollX = scrollX*(middleDistance/bottomDistance)
        val midScrollY = scrollY*(middleDistance/bottomDistance)
        binding.mid.scrollTo(-midScrollX,-midScrollY)
    }





}