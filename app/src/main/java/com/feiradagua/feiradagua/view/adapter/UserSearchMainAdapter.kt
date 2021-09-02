package com.feiradagua.feiradagua.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.RecyclerViewCartProductsBinding
import com.feiradagua.feiradagua.databinding.RecyclerViewProductsCardBinding
import com.feiradagua.feiradagua.model.`class`.Producer

class UserSearchMainAdapter(
    private val producerList: MutableList<Producer>,
    private val onCardClick: (Producer) -> Unit
): RecyclerView.Adapter<UserSearchMainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewProductsCardBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(producerList[position], onCardClick)
    }

    override fun getItemCount(): Int {
        return producerList.size
    }

    class ViewHolder(val binding: RecyclerViewProductsCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(producer: Producer, onCardClick: (Producer) -> Unit) {
            var text = ""
            Glide.with(itemView).load(producer.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivStore)
            binding.tvProducerNameTitle.text = producer.name
            producer.category.forEachIndexed {index, cat ->
                text = if(producer.category.size -1 == index) {
                    "$text $cat"
                }else {
                    "$text $cat,"
                }
            }
            binding.tvProducerCategoryTitle.text = text
            itemView.setOnClickListener {
                onCardClick(producer)
            }
        }
    }
}