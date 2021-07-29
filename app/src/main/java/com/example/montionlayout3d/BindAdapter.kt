package com.example.montionlayout3d


import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import kotlin.math.abs


object BindAdapter {

    var lastX : Float = 0f
    var lastY : Float = 0f

    val position = -35

    var lastProgress = 0f
    @BindingAdapter(value = ["SensorX","SensorY"],requireAll = true)
    @JvmStatic
    fun setProgress(view: MotionLayout, x:Float, y:Float) {
        lastProgress = view.progress
        //正常握持：x:-35  y:0
        //向左偏转： x:-45  y:-40
        //向右偏转; x：-25  y:35
/*        if (x+35 > 0){
            //向右偏转，progress减小
            val progress = (10/50) * (x+35)
            scrollMotion(view,progress)
        }else{

        }*/

        if (x in -10f..0f){
            val progress = abs((50-(x+35)*5)/100)
            scrollMotion(view,progress)
            Log.d("BindingAdapter",progress.toString())
        }


    }

    private fun scrollMotion(motionLayout: MotionLayout, progress: Float) {
        motionLayout.progress = progress
        Log.d("BindingAdapter-2",motionLayout.progress.toString())
    }


}