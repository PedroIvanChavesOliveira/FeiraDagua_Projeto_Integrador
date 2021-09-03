package com.feiradagua.feiradagua.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.RecyclerViewUserMyordersBinding
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.utils.getProducerInfos
import com.feiradagua.feiradagua.utils.setStatusFromOrder
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity

class UserHistoricMainAdapter(private val orderList: MutableList<Order>): RecyclerView.Adapter<UserHistoricMainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewUserMyordersBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int {
       return orderList.size
    }

    class ViewHolder(val binding: RecyclerViewUserMyordersBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(order: Order) {
            val producer = order.getProducerInfos(UserMenuActivity.PRODUCERS)
            Glide.with(itemView).load(producer.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProducer)
            binding.tvProducerName.text = producer.name
            binding.tvOrderDate.text = "Data de Entrega: ${order.deliveryDate}"
            binding.tvOrderStatus.setStatusFromOrder(order.confirmation)
        }
    }
}