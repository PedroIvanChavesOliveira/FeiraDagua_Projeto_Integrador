package com.feiradagua.feiradagua.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.RecyclerViewCartProductsBinding
import com.feiradagua.feiradagua.model.`class`.Cart

class UserShopCartMainAdapter(
    private var cartList: MutableList<Cart>,
    private val onCardClick: (Cart) -> Unit,
    private val onDeleteClick: (Cart) -> Unit
): RecyclerView.Adapter<UserShopCartMainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewCartProductsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartList[position], onCardClick, onDeleteClick)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class ViewHolder(val binding: RecyclerViewCartProductsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Cart, onCardClick: (Cart) -> Unit, onDeleteClick: (Cart) -> Unit) {
            Glide.with(itemView).load(item.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivStore)
            binding.tvProducerNameTitle.text = item.name
            binding.tvProducerCategoryTitle.text = "R$ %.2f".format(item.totalPrice)

            itemView.setOnClickListener {
                onCardClick(item)
            }

            binding.ibDeleteCart.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }
}