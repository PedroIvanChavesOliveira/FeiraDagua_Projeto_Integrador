package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.repository.StoreInfosRepository
import kotlinx.coroutines.launch

class StoreInfosViewModel: ViewModel() {
    var productsList: MutableLiveData<MutableList<Products>> = MutableLiveData()
    private val repository by lazy {
        StoreInfosRepository()
    }

    fun getProducts(id: String, /*lastDate: String*/) {
        viewModelScope.launch {
            productsList.postValue(repository.getProducerProducts(id))
        }
    }
}