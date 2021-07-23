package com.feiradagua.feiradagua.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.RecyclerViewCartProductsBinding
import com.feiradagua.feiradagua.model.`class`.Products

class ProducerOrderDetailsMainAdapter(
        private var products: MutableList<Products>
): RecyclerView.Adapter<ProducerOrderDetailsMainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewCartProductsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(val binding: RecyclerViewCartProductsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Products) {
            Glide.with(itemView).load(product.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivStore)
            binding.tvProducerNameTitle.text = product.name
            binding.tvProducerCategoryTitle.text = "Total: R$ ${product.price.toString()}"
        }
    }
}