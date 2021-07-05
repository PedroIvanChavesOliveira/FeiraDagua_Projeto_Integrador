package com.feiradagua.feiradagua.view.fragments.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserShopCartBinding

class UserShopCartFragment : Fragment() {
    private lateinit var binding: FragmentUserShopCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserShopCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}