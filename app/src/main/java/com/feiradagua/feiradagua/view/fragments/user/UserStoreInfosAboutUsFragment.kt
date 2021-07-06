package com.feiradagua.feiradagua.view.fragments.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserStoreInfosAboutUsBinding

class UserStoreInfosAboutUsFragment : Fragment() {
    private lateinit var binding: FragmentUserStoreInfosAboutUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserStoreInfosAboutUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}