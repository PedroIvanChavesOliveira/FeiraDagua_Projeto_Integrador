package com.feiradagua.feiradagua.view.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerMenuBinding
import com.feiradagua.feiradagua.view.fragments.producer.ProducerMyOrdersFragment
import com.feiradagua.feiradagua.view.fragments.producer.ProducerProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserSearchFragment
import com.feiradagua.feiradagua.view.fragments.user.UserShopCartFragment

class ProducerMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragments(ProducerMyOrdersFragment())
        startNavBar()
    }

    private fun startNavBar() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.myOrders -> {
                    initFragments(ProducerMyOrdersFragment())
                    true
                }
                R.id.profileProducer -> {
                    initFragments(ProducerProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun initFragments(fragment: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.flContainerHomeProducerActivity, fragment)
        frag.commit()
    }
}