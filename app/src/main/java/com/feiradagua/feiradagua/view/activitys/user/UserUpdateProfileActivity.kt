package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserUpdateProfileBinding

class UserUpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}