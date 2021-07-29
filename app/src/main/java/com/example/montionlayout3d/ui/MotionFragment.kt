package com.example.montionlayout3d.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.montionlayout3d.MainViewModel
import com.example.montionlayout3d.R
import com.example.montionlayout3d.SensorUtil
import com.example.montionlayout3d.databinding.FragmentMotionBinding

class MotionFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding : FragmentMotionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_motion, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewmodel = viewModel
        binding.sensor = SensorUtil
        binding.lifecycleOwner = requireActivity()
        initClick()
    }

    private fun initClick() {

    }


}