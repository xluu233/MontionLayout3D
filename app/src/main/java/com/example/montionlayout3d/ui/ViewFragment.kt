package com.example.montionlayout3d.ui

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.example.montionlayout3d.MainViewModel
import com.example.montionlayout3d.R
import com.example.montionlayout3d.databinding.FragmentCusviewBinding
import com.example.montionlayout3d.databinding.FragmentMainBinding
import com.example.montionlayout3d.view.Layout3D

class ViewFragment : Fragment() {


    private lateinit var viewModel: MainViewModel
    private lateinit var binding : FragmentCusviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCusviewBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initClick()
    }

    private fun initClick() {


//        val imageView = createImageView(context?.getDrawable(R.drawable.back)!!)
//
//        binding.frameLayout.addView(imageView)
    }


    @Synchronized
    private fun createImageView(drawable: Drawable): ImageView {
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val layout = inflater.inflate(R.layout.item, null)
//        val imageView: ImageView = layout.findViewById<ImageView>(R.id.image_3d)
//        val imageView = inflater.inflate(R.layout.item, null) as ImageView


        val imageView = ImageView(context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //val params = ViewGroup.LayoutParams(100.dp.toInt(), 100.dp.toInt())
        imageView.apply {
            layoutParams = params
            scaleType = ImageView.ScaleType.FIT_CENTER
            adjustViewBounds = true
            background = drawable
        }

        Layout3D.log("createImageView: ${imageView.measuredHeight} * ${imageView.measuredWidth}")
        return imageView
    }


}