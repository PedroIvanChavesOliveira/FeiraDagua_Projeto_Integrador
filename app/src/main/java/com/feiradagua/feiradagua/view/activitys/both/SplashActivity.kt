package com.feiradagua.feiradagua.view.activitys.both

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.feiradagua.feiradagua.databinding.ActivitySplashBinding
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION_SPLASH
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.viewModel.SplashViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModelSplash:  SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelSplash.getUser()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModelSplash.userOk.observe(this) {
                it?.let { user ->
                    viewModelSplash.getRegisteredDB()
                    viewModelSplash.registeredOk.observe(this) { doc ->
                        doc?.let { res ->
                            res.type?.let { type ->
                                if(type == 1) {
                                    initMenuUserActivity()
                                }else {
                                    initMenuProducerActivity()
                                }
                            }
                        }?: run {
                            initLoginActivity()
                        }
                    }
                }?: run {
                    initLoginActivity()
                }
            }
        }, 2000)
    }

    private fun initLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun initMenuUserActivity() {
        val intent = Intent(this, UserMenuActivity::class.java)
        intent.putExtra(POSITION_SPLASH, 1)
        startActivity(intent)
        finish()
    }

    private fun initMenuProducerActivity() {
        val intent = Intent(this, ProducerMenuActivity::class.java)
        intent.putExtra(POSITION_SPLASH, 1)
        startActivity(intent)
        finish()
    }
}