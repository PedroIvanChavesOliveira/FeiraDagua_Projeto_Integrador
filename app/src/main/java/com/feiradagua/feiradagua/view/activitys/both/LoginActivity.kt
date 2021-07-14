package com.feiradagua.feiradagua.view.activitys.both

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityLoginBinding
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.utils.Constants.Login.RC_SIGN_IN
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.viewModel.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.E

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var viewModelLogin = LoginViewModel()
    private val providers by lazy {
        arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.logo_feira_dagua_remove)
                .setTheme(R.style.LoginTheme)
                .build(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                viewModelLogin.getUser()
                viewModelLogin.getUserDb()
                viewModelLogin.userOk.observe(this) {
                    it?.let {user ->
//                        SplashActivity.CURRENT_USER = user
                        var photo = ""
                        if(user.photoUrl != null) {
                            photo = user.photoUrl.toString()
                        }
                        val setUser = User(user.displayName, user.email, user.phoneNumber, photo)
                        viewModelLogin.dbOk.observe(this) {doc ->
                            if(!doc.exists()) {
                                viewModelLogin.addUserOnDataBase(setUser)
                                startExtraInfosActivity()
                            }else {
                                viewModelLogin.getRegisteredDB()
                                viewModelLogin.registeredOk.observe(this) {reg ->
                                    reg?.let { type ->
                                        type.type?.let {
                                            when (it) {
                                                1 -> {
                                                    startMenuUserActivity()
                                                }
                                                2 -> {
                                                    startMenuProducerActivity()
                                                }
                                                else -> {
                                                    startExtraInfosActivity()
                                                }
                                            }
                                        }
                                    } ?: run {
                                        startExtraInfosActivity()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startExtraInfosActivity() {
        startActivity(Intent(this, ExtraInfosActivity::class.java))
        finish()
    }

    private fun startMenuUserActivity() {
        startActivity(Intent(this, UserMenuActivity::class.java))
        finish()
    }

    private fun startMenuProducerActivity() {
        startActivity(Intent(this, ProducerMenuActivity::class.java))
        finish()
    }
}