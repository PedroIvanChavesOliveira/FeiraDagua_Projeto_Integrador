package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityMenuUserBinding
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.view.fragments.user.UserProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserSearchFragment
import com.feiradagua.feiradagua.view.fragments.user.UserShopCartFragment
import com.feiradagua.feiradagua.viewModel.UserMenuViewModel

class UserMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuUserBinding
    private var viewModelUserMenu = UserMenuViewModel()

    companion object {
        lateinit var USER: User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelUserMenu = ViewModelProvider(this).get(UserMenuViewModel::class.java)

        setUserData()
    }

    private fun setUserData() {
        viewModelUserMenu.getUserDB()
        viewModelUserMenu.userInfo.observe(this) { user ->
            user?.let {
                USER = it
                initFragments(UserSearchFragment())
                startNavBar()
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

    private fun initFragments(fragment: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.flContainerHomeUserActivity, fragment)
        frag.commit()
    }
}