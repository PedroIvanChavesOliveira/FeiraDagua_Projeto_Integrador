package com.feiradagua.feiradagua.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.RecyclerViewPendingsOrdersBinding
import com.feiradagua.feiradagua.model.`class`.Order

class ProducerOrdersMainAdapter(
    private var ordersList: MutableList<Order>,
    private val onCardClick: (Order) -> Unit,
): RecyclerView.Adapter<ProducerOrdersMainAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewPendingsOrdersBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ordersList[position], onCardClick)
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class ViewHolder(val binding: RecyclerViewPendingsOrdersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order, onCardClick: (Order) -> Unit) {
            Glide.with(itemView).load(order.userPhoto).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivUser)
            binding.tvProducerNameTitle.text = order.username
            binding.tvDeliveryDateTitle.text = order.deliveryDate
            binding.tvValueTitle.text = order.totalPrice.toString()

            itemView.setOnClickListener {
                onCardClick(order)
            }
        }
    }
}