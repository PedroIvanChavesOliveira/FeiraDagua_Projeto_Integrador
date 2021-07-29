package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserSendOrderBinding

class UserSendOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSendOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSendOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}