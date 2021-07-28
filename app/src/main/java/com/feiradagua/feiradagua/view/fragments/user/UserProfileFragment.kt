package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserProfileBinding
import com.feiradagua.feiradagua.view.activitys.both.LoginActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserUpdateProfileActivity
import com.feiradagua.feiradagua.viewModel.UserProfileViewModel

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private val viewModelUserProfile: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btLogOutProfile.setOnClickListener {
            viewModelUserProfile.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.btEditProfile.setOnClickListener {
            startActivity(Intent(activity, UserUpdateProfileActivity::class.java))
        }

        setUserInfos()
    }

    override fun onResume() {
        super.onResume()

        setUserInfos()
    }

    private fun setUserInfos() {
        val user = UserMenuActivity.USER

        Glide.with(this).load(user.photo).into(binding.ivProfile)
        binding.tvNameValue.text = user.name
        binding.tvEmailValue.text = user.email
        binding.tvCellPhoneValue.text = user.phone
        binding.tvAdressValue.text = user.adress
    }
}