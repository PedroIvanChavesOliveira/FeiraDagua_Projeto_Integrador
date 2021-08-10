package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerMenuBinding
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.EXTRA_INFOS
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION_SPLASH
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.view.fragments.producer.ProducerMyOrdersFragment
import com.feiradagua.feiradagua.view.fragments.producer.ProducerNewProductFragment
import com.feiradagua.feiradagua.view.fragments.producer.ProducerProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserProfileFragment
import com.feiradagua.feiradagua.view.fragments.user.UserSearchFragment
import com.feiradagua.feiradagua.view.fragments.user.UserShopCartFragment
import com.feiradagua.feiradagua.viewModel.ProducerMenuViewModel

class ProducerMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerMenuBinding
    private val viewModelProducerMenu: ProducerMenuViewModel by viewModels()
    private var getFirstLogin = 0
    private var startTutorial = false
    private var positionFromSplash = 0

    companion object {
        lateinit var PRODUCER: Producer
        var PRODUCTS: MutableList<Products>? = mutableListOf()
        var ORDERS: MutableList<Order>? = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getFirstLogin = intent.getIntExtra(EXTRA_INFOS, 0)
        startTutorial = intent.getBooleanExtra(TUTORIAL, false)
        positionFromSplash = intent.getIntExtra(POSITION_SPLASH, 0)
        viewModelProducerMenu.updateToken()

        if(startTutorial) {
            when(getFirstLogin) {
                1 -> { initFragmentsTutorial(ProducerNewProductFragment()) }
                2 -> { initFragmentsTutorial(ProducerNewProductFragment()) }
                3 -> { initFragmentsTutorial(ProducerMyOrdersFragment()) }
                4 -> { initFragmentsTutorial(ProducerProfileFragment()) }
                else -> { setProducerInfos() }
            }
        }else {
            when(positionFromSplash) {
                1 -> { setProducerInfos() }
                2 -> { setProducerInfos() }
                else -> {
                    initFragments(ProducerMyOrdersFragment())
                    startNavBar()
                }
            }
        }
    }

    private fun setProducerInfos() {
        viewModelProducerMenu.getProducerDB()
        viewModelProducerMenu.getOrdersDB()
        viewModelProducerMenu.getProductsDB()
        viewModelProducerMenu.producerInfo.observe(this) { producer ->
            producer?.let {
                PRODUCER = it
                viewModelProducerMenu.products.observe(this) { products ->
                    PRODUCTS = products
                    viewModelProducerMenu.orders.observe(this) { orders ->
                        ORDERS = orders
                        initFragments(ProducerMyOrdersFragment())
                        startNavBar()
                    }
                }
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
                R.id.myProducts -> {
                    initFragments(ProducerNewProductFragment())
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

    private fun initFragmentsTutorial(fragment: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putBoolean(TUTORIAL, true)
        fragment.arguments = bundle
        frag.replace(R.id.flContainerHomeProducerActivity, fragment)
        frag.commit()
    }
}