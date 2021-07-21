package com.feiradagua.feiradagua.view.fragments.producer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentProducerMyOrdersBinding
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.utils.Constants.Intents.ORDER_DETAILS
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerOrderDetailsActivity
import com.feiradagua.feiradagua.view.adapter.ProducerOrdersMainAdapter

class ProducerMyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentProducerMyOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProducerMyOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOrdersRecyclerView()
    }

    private fun setUpOrdersRecyclerView() {
        ProducerMenuActivity.ORDERS?.let { orders ->
            binding.rvStoreHome.apply {
                layoutManager = LinearLayoutManager(activity)
                activity?.let {
                    adapter = ProducerOrdersMainAdapter(orders) { orderInfo ->
                        startProducerOrdersDetails(orderInfo)
                    }
                }
            }
            binding.tvOrderNotFound.isVisible = false
        }?: run {
            binding.rvStoreHome.isVisible = false
        }
    }

    private fun startProducerOrdersDetails(order: Order) {
        val intent = Intent(activity, ProducerOrderDetailsActivity::class.java)
        intent.putExtra(ORDER_DETAILS, order)
        startActivity(intent)
    }
}