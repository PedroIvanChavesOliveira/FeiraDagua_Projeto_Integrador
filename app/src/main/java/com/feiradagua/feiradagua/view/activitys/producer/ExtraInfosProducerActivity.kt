package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityExtraInfosProducerBinding

class ExtraInfosProducerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtraInfosProducerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraInfosProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}