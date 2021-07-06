package com.feiradagua.feiradagua.view.activitys.user

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserStoreInfosBinding
import com.feiradagua.feiradagua.view.fragments.user.UserStoreInfosAboutUsFragment
import com.feiradagua.feiradagua.view.fragments.user.UserStoreInfosProductsFragment
import com.google.android.material.tabs.TabLayout

class UserStoreInfosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserStoreInfosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserStoreInfosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragments(UserStoreInfosProductsFragment())
        startMenu()
    }

    private fun startMenu() {
        binding.tabLayout.addOnTabSelectedListener (object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                        initFragments(UserStoreInfosProductsFragment())
                    }
                    1 -> {
                        initFragments(UserStoreInfosAboutUsFragment())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun initFragments(fragment: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.flContainerStoreInfoActivity, fragment)
        frag.commit()
    }
}