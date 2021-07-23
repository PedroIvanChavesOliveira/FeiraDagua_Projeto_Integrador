package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.databinding.ActivityProducerUpdateProfileBinding

class ProducerUpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}