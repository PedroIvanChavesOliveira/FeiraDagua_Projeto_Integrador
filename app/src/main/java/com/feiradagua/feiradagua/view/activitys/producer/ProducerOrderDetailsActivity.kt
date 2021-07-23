package com.feiradagua.feiradagua.view.activitys.producer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerOrderDetailsBinding
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants.Intents.ORDER_DETAILS
import com.feiradagua.feiradagua.view.adapter.ProducerOrderDetailsMainAdapter

class ProducerOrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerOrderDetailsBinding
    var getOrderInfos: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getOrderInfos = intent.getParcelableExtra(ORDER_DETAILS)

        setUpOrderDetails(getOrderInfos)
    }

    private fun setUpOrderDetails(order: Order?) {
        val productsInOrder = mutableListOf<Products>()
        order?.let { infos ->
            ProducerMenuActivity.PRODUCTS?.let { productsList ->
                productsList.forEach { product ->
                    if(infos.products.contains(product.id)) {
                        productsInOrder.add(product)
                    }
                }
            }

            Glide.with(this).load(infos.userPhoto).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivClient)
            binding.tvClientValue.text = infos.username
            binding.tvDateValueOrderDetails.text = infos.deliveryDate
            binding.tvTotalValueOrderDetails.text = infos.totalPrice.toString()
            binding.rvOrderDetails.apply {
                layoutManager = LinearLayoutManager(this@ProducerOrderDetailsActivity)
                adapter = ProducerOrderDetailsMainAdapter(productsInOrder)
            }
        }
    }
}