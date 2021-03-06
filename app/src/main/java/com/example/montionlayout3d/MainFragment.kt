package com.example.montionlayout3d

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.montionlayout3d.databinding.FragmentMainBinding

class MainFragment : Fragment() {

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
        initClick()
    }

    private fun initClick() {

        binding.button1.setOnClickListener {
            navigate(R.id.action_mainFragment_to_motionFragment)
        }

        binding.button2.setOnClickListener {
            navigate(R.id.action_mainFragment_to_viewFragment)
        }

        binding.button3.setOnClickListener {
            navigate(R.id.action_mainFragment_to_viewFragment2)
        }
    }




    fun navigate(destination: Int, bundle: Bundle ?= null) = NavHostFragment.findNavController(this).apply {
        currentDestination?.getAction(destination)?.let {
            navigate(destination,bundle)
        }
    }

}