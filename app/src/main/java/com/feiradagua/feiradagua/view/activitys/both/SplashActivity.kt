package com.feiradagua.feiradagua.view.activitys.both

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.databinding.ActivitySplashBinding
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.viewModel.SplashViewModel
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var viewModelSplash = SplashViewModel()

//    companion object {
//        lateinit var CURRENT_USER: FirebaseUser
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelSplash = ViewModelProvider(this).get(SplashViewModel::class.java)
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
//                    CURRENT_USER = user
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
        startActivity(Intent(this, UserMenuActivity::class.java))
        finish()
    }

    private fun initMenuProducerActivity() {
        startActivity(Intent(this, ProducerMenuActivity::class.java))
        finish()
    }
}