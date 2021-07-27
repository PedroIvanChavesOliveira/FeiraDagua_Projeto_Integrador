package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityExtraInfosProducerBinding
import com.feiradagua.feiradagua.databinding.FragmentUserStoreInfosProductsBinding
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_INFO
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserProductInfoActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.feiradagua.feiradagua.view.adapter.StoreInfosProductMainAdapter
import java.net.URLEncoder

class UserStoreInfosProductsFragment : Fragment() {
    private lateinit var binding: FragmentUserStoreInfosProductsBinding
    private var getId: String? = ""
    private var producerPhone: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserStoreInfosProductsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getId = arguments?.getString(PRODUCER_ID)

        binding.floatingButtonWhatsApp.setOnClickListener {
            UserMenuActivity.PRODUCERS.forEach { producer ->
                if(producer.uid == getId) {
                    producerPhone = producer.phone
                }
            }
            startWhatsAppChat(producerPhone)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvStoreHome.apply {
            UserStoreInfosActivity.PRODUCTS.let {list ->
                list[getId]?.let {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = StoreInfosProductMainAdapter(it) { startProductInfosActivity(it) }
                }
            }
        }
    }

    private fun startProductInfosActivity(prod: Products) {
        val intent = Intent(activity, UserProductInfoActivity::class.java)
        intent.putExtra(PRODUCT_INFO, prod)
        intent.putExtra(POSITION, 2)
        startActivity(intent)
    }

    private fun startWhatsAppChat(phone: String?) {
        phone?.let {
            val contact = "+55 $phone"
            val message = "Ola Tubs, um abraço do Feira Dagua!"
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$contact&text=${URLEncoder.encode(message,"UTF-8")}"

            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}