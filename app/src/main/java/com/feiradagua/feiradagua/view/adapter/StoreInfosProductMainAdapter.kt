package com.feiradagua.feiradagua.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.RecyclerViewProductsCardBinding
import com.feiradagua.feiradagua.model.`class`.Products

class StoreInfosProductMainAdapter(
    private val productsList: MutableList<Products>,
    private val onCardClick: (Products) -> Unit
): RecyclerView.Adapter<StoreInfosProductMainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewProductsCardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productsList[position], onCardClick)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class ViewHolder(val binding: RecyclerViewProductsCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Products, onCardClick: (Products) -> Unit) {
            Glide.with(itemView).load(product.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivStore)
            binding.tvProducerNameTitle.text = product.name
            binding.tvProducerCategoryTitle.text = "R$ %.2f".format(product.price)

            itemView.setOnClickListener {
                onCardClick(product)
            }
        }
    }
}