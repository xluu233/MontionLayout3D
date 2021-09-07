package com.example.montionlayout3d.ui

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.montionlayout3d.databinding.FragmentCusviewBinding
import com.example.montionlayout3d.view.Layout3D

class CustomViewFragment : Fragment() {

    private lateinit var binding : FragmentCusviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCusviewBinding.inflate(inflater)
        return binding.root
    }


}