package com.feiradagua.feiradagua.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.RecyclerViewProductsUpdateBinding
import com.feiradagua.feiradagua.model.`class`.Products

class ProducerNewProductMainAdapter(
        private val products: MutableList<Products>,
        private val onCardClick: (Products) -> Unit,
        private val onDeleteClick: (Products) -> Unit,
        private val onUpdateClick: (Products) -> Unit
): RecyclerView.Adapter<ProducerNewProductMainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewProductsUpdateBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position], onCardClick, onDeleteClick, onUpdateClick)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ViewHolder(val binding: RecyclerViewProductsUpdateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Products, onCardClick: (Products) -> Unit,
                 onDeleteClick: (Products) -> Unit, onUpdateClick: (Products) -> Unit) {

            Glide.with(itemView).load(product.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivStore)
            binding.tvProducerCategoryTitle.text = "R$ %.2f".format(product.price)
            binding.tvProducerNameTitle.text = product.name

            itemView.setOnClickListener {
                onCardClick(product)
            }

            binding.ibDelete.setOnClickListener {
                onDeleteClick(product)
            }
            binding.ibUpdate.setOnClickListener {
                onUpdateClick(product)
            }
        }
    }
}