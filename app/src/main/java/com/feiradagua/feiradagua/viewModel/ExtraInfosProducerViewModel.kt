package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.repository.ExtraInfosProducerRepository
import kotlinx.coroutines.launch

class ExtraInfosProducerViewModel: ViewModel() {
    private val repository by lazy {
        ExtraInfosProducerRepository()
    }

    fun setExtraInfosDB(description: String, deliveryDate: MutableList<String>, deliveryLocation: MutableList<String>, payment: MutableList<String>) {
        viewModelScope.launch {
            repository.setExtraInfosDB(description, deliveryDate, deliveryLocation, payment)
        }
    }
}