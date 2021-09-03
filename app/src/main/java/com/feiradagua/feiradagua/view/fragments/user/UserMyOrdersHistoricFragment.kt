package com.feiradagua.feiradagua.view.fragments.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserMyOrdersHistoricBinding
import com.feiradagua.feiradagua.databinding.FragmentUserProfileBinding
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.adapter.UserHistoricMainAdapter

class UserMyOrdersHistoricFragment : Fragment() {
    private lateinit var binding: FragmentUserMyOrdersHistoricBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserMyOrdersHistoricBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val orderList = UserMenuActivity.HISTORIC
        if(orderList.isNotEmpty()) {
            binding.rvUserOrders.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = UserHistoricMainAdapter(orderList)
            }
        }
    }
}