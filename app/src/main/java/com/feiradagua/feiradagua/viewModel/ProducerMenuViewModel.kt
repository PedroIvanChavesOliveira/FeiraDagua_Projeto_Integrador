package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.repository.ProducerMenuRepository
import kotlinx.coroutines.launch

class ProducerMenuViewModel: ViewModel() {
    var producerInfo: MutableLiveData<Producer?> = MutableLiveData()
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
}