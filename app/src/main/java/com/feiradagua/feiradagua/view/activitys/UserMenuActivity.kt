package com.feiradagua.feiradagua.view.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityMenuUserBinding
import com.feiradagua.feiradagua.view.fragments.user.UserProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserSearchFragment
import com.feiradagua.feiradagua.view.fragments.user.UserShopCartFragment

class UserMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragments(UserSearchFragment())
        startNavBar()
    }

    private fun startNavBar() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
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