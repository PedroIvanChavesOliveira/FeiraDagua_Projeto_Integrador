package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.repository.ProducerMenuRepository
import kotlinx.coroutines.launch

class ProducerMenuViewModel: ViewModel() {
    var producerInfo: MutableLiveData<Producer?> = MutableLiveData()
    var products: MutableLiveData<MutableList<Products>?> = MutableLiveData()
    var orders: MutableLiveData<MutableList<Order>?> = MutableLiveData()
    private val repository by lazy {
        ProducerMenuRepository()
    }

    fun getProducerDB() {
        viewModelScope.launch {
            repository.getProducerDb()?.let {
                producerInfo.postValue(it.toObject(Producer::class.java))
            }?: run {
                producerInfo.postValue(null)
            }
        }
    }

    fun updateToken() {
        viewModelScope.launch {
            repository.updateToken()
        }
    }

    fun getProductsDB() {
        viewModelScope.launch {
            repository.getProductsDB()?.let {
//                val productsList = mutableListOf<Products>()
//                it.documents.forEach { doc ->
//                    doc.toObject(Products::class.java)?.let { products ->
//                        productsList.add(products)
//                    }
//                }
//                products.postValue(productsList)
                products.postValue(it.toObjects(Products::class.java))
            }?: run {
                products.postValue(null)
            }
        }
    }

    fun getOrdersDB() {
        viewModelScope.launch {
            repository.getOrdersDB()?.let {
//                val ordersList = mutableListOf<Order>()
//                it.documents.forEach { doc ->
//                    doc.toObject(Order::class.java)?.let { order ->
//                        ordersList.add(order)
//                    }
//                }
//                orders.postValue(ordersList)
                orders.postValue(it.toObjects(Order::class.java))
            }?: run {
                orders.postValue(null)
            }
        }
    }
}