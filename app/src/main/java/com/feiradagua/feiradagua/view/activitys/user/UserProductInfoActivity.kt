package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.feiradagua.feiradagua.databinding.ActivityUserProductInfoBinding

class UserProductInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProductInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}