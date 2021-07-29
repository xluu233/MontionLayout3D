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
import com.example.montionlayout3d.databinding.FragmentCusviewBinding
import com.example.montionlayout3d.databinding.FragmentMainBinding

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


    }


}