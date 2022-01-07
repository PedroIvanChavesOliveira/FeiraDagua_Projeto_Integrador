package com.feiradagua.feiradagua.view.activitys.both

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityLoginBinding
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION_SPLASH
import com.feiradagua.feiradagua.utils.Constants.Login.RC_SIGN_IN
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.viewModel.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModelLogin: LoginViewModel by viewModels()
    private val providers by lazy {
        listOf(AuthUI.IdpConfig.EmailBuilder().build())
    }
    private val startUI = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            viewModelLogin.getUser()
            viewModelLogin.getUserDb()
            viewModelLogin.getToken()
            viewModelLogin.userOk.observe(this) {
                viewModelLogin.token.observe(this) { token ->
                    it?.let {user ->
                        var photo = ""
                        if(user.photoUrl != null) {
                            photo = user.photoUrl.toString()
                        }
                        setUpUserAndStartNavigation(user, photo, token)
                    }
                }
            }
        }else {
            result.data?.extras
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btInitLogin.setOnClickListener {
            startUI.launch(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.logo_feira_dagua_remove)
                .setTheme(R.style.LoginTheme)
                .build())
        }
    }

    private fun setUpUserAndStartNavigation(user: FirebaseUser, photo: String, token: String) {
        val setUser = User(user.uid, user.displayName, user.email, user.phoneNumber, photo, token=token)
        viewModelLogin.dbOk.observe(this@LoginActivity) {doc ->
            if(!doc.exists()) {
                viewModelLogin.addUserOnDataBase(setUser)
                startExtraInfosActivity()
            }else {
                viewModelLogin.getRegisteredDB()
                viewModelLogin.registeredOk.observe(this@LoginActivity) {reg ->
                    reg?.let { type ->
                        type.type?.let {
                            when (it) {
                                1 -> { startMenuUserActivity() }
                                2 -> { startMenuProducerActivity() }
                                else -> { startExtraInfosActivity() }
                            }
                        }
                    } ?: run {
                        startExtraInfosActivity()
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
        val intent = Intent(this, UserMenuActivity::class.java)
        intent.putExtra(POSITION_SPLASH, 2)
        startActivity(intent)
        finish()
    }

    private fun startMenuProducerActivity() {
        val intent = Intent(this, ProducerMenuActivity::class.java)
        intent.putExtra(POSITION_SPLASH, 2)
        startActivity(intent)
        finish()
    }
}