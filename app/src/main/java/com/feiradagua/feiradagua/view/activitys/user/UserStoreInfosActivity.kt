package com.feiradagua.feiradagua.view.activitys.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserStoreInfosBinding
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences.getLastModifiedPreferences
import com.feiradagua.feiradagua.view.fragments.user.UserStoreInfosAboutUsFragment
import com.feiradagua.feiradagua.view.fragments.user.UserStoreInfosProductsFragment
import com.feiradagua.feiradagua.viewModel.StoreInfosViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.tabs.TabLayout

class UserStoreInfosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserStoreInfosBinding
    private val viewModelStoreInfos: StoreInfosViewModel by viewModels()
    private var tutorial = false
    private var tutorialPos = 0
    private var getProducerId = ""

    companion object {
        var PRODUCTS: MutableMap<String, MutableList<Products>> = mutableMapOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserStoreInfosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tutorial = intent.getBooleanExtra(TUTORIAL, false)
        tutorialPos = intent.getIntExtra(POSITION, 0)
        getProducerId = intent.getStringExtra(PRODUCER_ID).toString()

        if(tutorial) {
            when(tutorialPos) {
                1 -> {
                    initFragmentsTutorial(UserStoreInfosProductsFragment())
                }
                else -> {
                    initFragmentsTutorial(UserStoreInfosAboutUsFragment())
                }
            }
        }else {
            startFragments()
            setArrowBack()
        }
    }

    private fun setArrowBack() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun startFragments() {
        if(PRODUCTS.isEmpty() || !PRODUCTS.containsKey(getProducerId)) {
//            val lastModified = getLastModifiedPreferences(this, 3)
//            lastModified?.let { viewModelStoreInfos.getProducts(getProducerId,lastModified) }
            viewModelStoreInfos.getProducts(getProducerId)
            viewModelStoreInfos.productsList.observe(this) {
                PRODUCTS[getProducerId] = it
                initFragments(UserStoreInfosProductsFragment())
                startMenu()
            }
        }else {
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

    private fun initFragmentsTutorial(fragment: Fragment) {
        val frag = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString(PRODUCER_ID, getProducerId)
        bundle.putBoolean(TUTORIAL, true)
        fragment.arguments = bundle
        frag.replace(R.id.flContainerStoreInfoActivity, fragment)
        frag.commit()
    }
}