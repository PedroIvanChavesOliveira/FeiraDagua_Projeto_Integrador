package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserStoreInfosBinding
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.view.fragments.user.UserStoreInfosAboutUsFragment
import com.feiradagua.feiradagua.view.fragments.user.UserStoreInfosProductsFragment
import com.feiradagua.feiradagua.viewModel.StoreInfosViewModel
import com.google.android.material.tabs.TabLayout

class UserStoreInfosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserStoreInfosBinding
    private val viewModelStoreInfos: StoreInfosViewModel by viewModels()
    private var getProducerId = ""

    companion object {
        var PRODUCTS: MutableMap<String, MutableList<Products>> = mutableMapOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserStoreInfosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProducerId = intent.getStringExtra(PRODUCER_ID).toString()
        startFragments()
    }

    private fun startFragments() {
        viewModelStoreInfos.getProducts(getProducerId)
        viewModelStoreInfos.productsList.observe(this) {
            if(PRODUCTS.isEmpty() || !PRODUCTS.containsKey(getProducerId)) {
                PRODUCTS[getProducerId] = it
            }
            initFragments(UserStoreInfosProductsFragment())
            startMenu()
        }
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
        val bundle = Bundle()
        bundle.putString(PRODUCER_ID, getProducerId)
        fragment.arguments = bundle
        frag.replace(R.id.flContainerStoreInfoActivity, fragment)
        frag.commit()
    }
}