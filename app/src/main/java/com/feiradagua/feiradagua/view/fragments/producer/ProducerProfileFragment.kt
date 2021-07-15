package com.feiradagua.feiradagua.view.fragments.producer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentProducerProfileBinding
import com.feiradagua.feiradagua.view.activitys.both.LoginActivity
import com.feiradagua.feiradagua.viewModel.ProducerProfileViewModel

class ProducerProfileFragment : Fragment() {
    private lateinit var binding: FragmentProducerProfileBinding
    private var viewModelProducerProfile = ProducerProfileViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProducerProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelProducerProfile = ViewModelProvider(this).get(ProducerProfileViewModel::class.java)

        binding.btLogOutProfile.setOnClickListener {
            viewModelProducerProfile.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    private fun setProducerInfos() {}
}