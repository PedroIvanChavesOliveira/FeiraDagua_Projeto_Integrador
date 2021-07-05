package com.feiradagua.feiradagua.view.fragments.producer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentProducerProfileBinding

class ProducerProfileFragment : Fragment() {
    private lateinit var binding: FragmentProducerProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProducerProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}