package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerOrderDetailsBinding

class ProducerOrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}