package com.example.montionlayout3d.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.montionlayout3d.MainViewModel
import com.example.montionlayout3d.R
import com.example.montionlayout3d.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private lateinit var viewModel: MainViewModel
    private lateinit var binding : FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initClick()
    }

    private fun initClick() {

        binding.button1.setOnClickListener {
            navigate(R.id.action_mainFragment_to_motionFragment)
        }

        binding.button2.setOnClickListener {
            navigate(R.id.action_mainFragment_to_viewFragment)
        }

    }



    fun navigate(destination: Int, bundle: Bundle ?= null) = NavHostFragment.findNavController(this).apply {
        currentDestination?.getAction(destination)?.let {
            navigate(destination,bundle)
        }
    }

}