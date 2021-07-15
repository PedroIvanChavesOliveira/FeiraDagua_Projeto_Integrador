package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerMenuBinding
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.view.fragments.producer.ProducerMyOrdersFragment
import com.feiradagua.feiradagua.view.fragments.producer.ProducerProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserSearchFragment
import com.feiradagua.feiradagua.view.fragments.user.UserShopCartFragment
import com.feiradagua.feiradagua.viewModel.ProducerMenuViewModel

class ProducerMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerMenuBinding
    private var viewModelProducerMenu = ProducerMenuViewModel()

    companion object {
        lateinit var PRODUCER: Producer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelProducerMenu = ViewModelProvider(this).get(ProducerMenuViewModel::class.java)

        setProducerInfos()
    }

    private fun setProducerInfos() {
        viewModelProducerMenu.getProducerDB()
        viewModelProducerMenu.producerInfo.observe(this) { producer ->
            producer?.let {
                PRODUCER = it
                initFragments(ProducerMyOrdersFragment())
                startNavBar()
            }
        }
    }

    private fun startNavBar() {
        binding.bottomNavigation.setOnItemSelectedListener {
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