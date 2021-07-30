package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityMenuUserBinding
import com.feiradagua.feiradagua.model.`class`.Cart
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION_SPLASH
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.view.fragments.user.UserProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserSearchFragment
import com.feiradagua.feiradagua.view.fragments.user.UserShopCartFragment
import com.feiradagua.feiradagua.viewModel.UserMenuViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence

class UserMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuUserBinding
    private val viewModelUserMenu: UserMenuViewModel by viewModels()
    private var positionFromSplash = 0
    private var startTutorial = false
    private var tutorialPosition = 0

    companion object {
        lateinit var USER: User
        lateinit var PRODUCERS: MutableList<Producer>
        var MY_CART: MutableList<Cart> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelUserMenu.updateToken()
        startTutorial = intent.getBooleanExtra(TUTORIAL, false)
        tutorialPosition = intent.getIntExtra(POSITION, 0)
        positionFromSplash = intent.getIntExtra(POSITION_SPLASH, 0)

        if(startTutorial) {
            when(tutorialPosition) {
                1 -> {
                    initFragmentsTutorial(UserShopCartFragment())
                }
                2 -> {
                    initFragmentsTutorial(UserProfileFragment())
                }
                else -> {
                    setUserDataTutorial()
                }
            }
        }else {
            when(positionFromSplash) {
                1 -> setUserData()
                else -> {
                    initFragments(UserSearchFragment())
                    startNavBar()
                }
            }
        }
    }

    private fun setUserData() {
        viewModelUserMenu.getUserDB()
        viewModelUserMenu.getProducers()
        viewModelUserMenu.userInfo.observe(this) { user ->
            user?.let {
                USER = it
                viewModelUserMenu.producerList.observe(this) { producer ->
                    if(!producer.isNullOrEmpty()) {
                        PRODUCERS = producer
                    }
                    initFragments(UserSearchFragment())
                    startNavBar()
                }
            }
        }
    }

    private fun setUserDataTutorial() {
        viewModelUserMenu.getUserDB()
        viewModelUserMenu.getProducers()
        viewModelUserMenu.userInfo.observe(this) { user ->
            user?.let {
                USER = it
                viewModelUserMenu.producerList.observe(this) { producer ->
                    if(!producer.isNullOrEmpty()) {
                        PRODUCERS = producer
                    }
                    initFragmentsTutorial(UserSearchFragment())
                }
            }
        }
    }

    private fun startNavBar() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.search -> {
                    initFragments(UserSearchFragment())
                    true
                }
                R.id.shopCart -> {
                    initFragments(UserShopCartFragment())
                    true
                }
                R.id.profileUser -> {
                    initFragments(UserProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun initFragmentsTutorial(fragment: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putBoolean(TUTORIAL, true)
        fragment.arguments = bundle
        frag.replace(R.id.flContainerHomeUserActivity, fragment)
        frag.commit()
    }

    private fun initFragments(fragment: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.flContainerHomeUserActivity, fragment)
        frag.commit()
    }
}