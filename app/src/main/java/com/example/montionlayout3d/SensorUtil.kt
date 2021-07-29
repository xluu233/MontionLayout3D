package com.example.montionlayout3d

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.databinding.ObservableFloat
import java.util.*


/**
 * @ClassName SensorUtil
 * @Description TODO
 * @Author AlexLu_1406496344@qq.com
 * @Date 2021/7/29 16:03
 */
object SensorUtil {

    private var mAcceleValues : FloatArray ?= null
    private var mMageneticValues : FloatArray ?= null

    val x = ObservableFloat(0f)
    val y = ObservableFloat(0f)

    //上次更新数据时间
    var updateTime : Long = 0L

    private val listener = object : SensorEventListener{
        override fun onSensorChanged(event: SensorEvent?) {
            //Log.d("onSensorChanged",event.toString())
            if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                mAcceleValues = event.values
            }
            if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                mMageneticValues = event.values
            }

            if (mAcceleValues==null || mMageneticValues==null) return

            val values = FloatArray(3)
            val R = FloatArray(9)
            SensorManager.getRotationMatrix(R, null, mAcceleValues, mMageneticValues);
            SensorManager.getOrientation(R, values);

/*            if (System.currentTimeMillis() - updateTime >= 500){
                // x轴的偏转角度
                x.set(Math.toDegrees(values[1].toDouble()).toFloat())
                // y轴的偏转角度
                y.set(Math.toDegrees(values[2].toDouble()).toFloat())
                updateTime = System.currentTimeMillis()
            }*/

            // x轴的偏转角度
            x.set(Math.toDegrees(values[1].toDouble()).toFloat())
            // y轴的偏转角度
            y.set(Math.toDegrees(values[2].toDouble()).toFloat())

            Log.d("mAcceleValues","x:${x.get()} y:${y.get()}")
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            //Log.d("onAccuracyChanged","sensor:${sensor.toString()} -----------      accuracy:${accuracy}")
        }
    }

    fun initSensor(context: Context){
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 重力传感器
        val acceleSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(listener, acceleSensor, SensorManager.SENSOR_DELAY_NORMAL)


        // 地磁场传感器
        val magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(listener, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }


}