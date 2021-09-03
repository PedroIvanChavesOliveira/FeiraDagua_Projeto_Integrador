package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityMenuUserBinding
import com.feiradagua.feiradagua.model.`class`.*
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION_SPLASH
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences.getLastModifiedPreferences
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences.setLastModifiedPreferences
import com.feiradagua.feiradagua.view.fragments.user.UserMyOrdersHistoricFragment
import com.feiradagua.feiradagua.view.fragments.user.UserProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserSearchFragment
import com.feiradagua.feiradagua.view.fragments.user.UserShopCartFragment
import com.feiradagua.feiradagua.viewModel.UserMenuViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import java.util.*

class UserMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuUserBinding
    private val viewModelUserMenu: UserMenuViewModel by viewModels()
    private var positionFromSplash = 0
    private var startTutorial = false
    private var tutorialPosition = 0

    companion object {
        lateinit var USER: User
        var PRODUCERS: MutableList<Producer>? = mutableListOf()
        var MY_CART: MutableList<Cart> = mutableListOf()
        var HISTORIC: MutableList<Order> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelUserMenu.updateToken()
//        viewModelUserMenu.insertUserDateInRoom(this)
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
                2 -> setUserData()
                else -> {
                    initFragments(UserSearchFragment())
                    startNavBar()
                }
            }
        }
    }

    private fun setUserData() {
//        val userLastModified = getLastModifiedPreferences(this, 1)
//        userLastModified?.let { viewModelUserMenu.getUserDB(userLastModified) }
        viewModelUserMenu.getUserDB()
        viewModelUserMenu.getHistoricDb()
        viewModelUserMenu.userInfo.observe(this) { user ->
            user?.let {
                USER = it
//                val producersLastModified = getLastModifiedPreferences(this, 4)
//                producersLastModified?.let {date -> viewModelUserMenu.getProducers(it.deliveryArea, producersLastModified) }
                viewModelUserMenu.getProducers(it.deliveryArea, this, it.uid)
                viewModelUserMenu.producerList.observe(this) { producer ->
                    if(!producer.isNullOrEmpty()) {
                        PRODUCERS = producer
                    }
                    viewModelUserMenu.orderList.observe(this) { orderList ->
                        orderList.sortBy { it.deliveryDate }
                        HISTORIC = orderList
                        initFragments(UserSearchFragment())
                        startNavBar()
                    }
//                    viewModelUserMenu.insertUserDateInRoom(this)
//                    setLastModifiedPreferences(this, 4, Calendar.getInstance().time.toString())
                }
            }
        }
    }

    private fun setUserDataTutorial() {
//        val userLastModified = getLastModifiedPreferences(this, 1)
//        userLastModified?.let { viewModelUserMenu.getUserDB(userLastModified) }
        viewModelUserMenu.getUserDB()
        viewModelUserMenu.getHistoricDb()
        viewModelUserMenu.userInfo.observe(this) { user ->
            user?.let {
                USER = it
//                val producersLastModified = getLastModifiedPreferences(this, 4)
//                producersLastModified?.let {date -> viewModelUserMenu.getProducers(it.deliveryArea, producersLastModified) }
                viewModelUserMenu.getProducers(it.deliveryArea, this, it.uid)
                viewModelUserMenu.producerList.observe(this) { producer ->
                    if(!producer.isNullOrEmpty()) {
                        PRODUCERS = producer
                    }
                    viewModelUserMenu.orderList.observe(this) { orderList ->
                        HISTORIC = orderList
                        initFragments(UserSearchFragment())
                    }
//                    setLastModifiedPreferences(this, 4, Calendar.getInstance().time.toString())
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
                R.id.historic -> {
                    initFragments(UserMyOrdersHistoricFragment())
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