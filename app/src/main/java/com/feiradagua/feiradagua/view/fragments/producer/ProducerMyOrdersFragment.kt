package com.feiradagua.feiradagua.view.fragments.producer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentProducerMyOrdersBinding

class ProducerMyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentProducerMyOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProducerMyOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}