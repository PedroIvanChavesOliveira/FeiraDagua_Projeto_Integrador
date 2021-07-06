package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerNewProductBinding

class ProducerNewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerNewProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}