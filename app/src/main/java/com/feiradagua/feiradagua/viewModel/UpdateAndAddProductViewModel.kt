package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.repository.UpdateAndAddProductRepository
import kotlinx.coroutines.launch

class UpdateAndAddProductViewModel: ViewModel() {
    val insertOk: MutableLiveData<Boolean> = MutableLiveData()
    private val repository by lazy {
        UpdateAndAddProductRepository()
    }

    fun addAndUpdateProduct(id: String, product: Products) {
        viewModelScope.launch {
            insertOk.postValue(repository.addNewProduct(id, product))
        }
    }
}