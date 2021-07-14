package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.databinding.ActivityProducerUpdateAndAddProductBinding

class ProducerUpdateAndAddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerUpdateAndAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerUpdateAndAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}