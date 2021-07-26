package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.business.ProducerUpdateProfileBusiness
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.cep.ViaCep
import com.feiradagua.feiradagua.repository.ProducerUpdateProfileRepository
import kotlinx.coroutines.launch

class ProducerUpdateProfileViewModel: ViewModel() {
    var updateProducer: MutableLiveData<Boolean> = MutableLiveData()
    val viaCepResponseSuccess: MutableLiveData<ViaCep> = MutableLiveData()
    val viaCepResponseFailure: MutableLiveData<String> = MutableLiveData()
    private val repository by lazy {
        ProducerUpdateProfileRepository()
    }

    private val business by lazy {
        ProducerUpdateProfileBusiness()
    }

    fun updateProducer(producer: Producer) {
        viewModelScope.launch {
            updateProducer.postValue(repository.updateProducer(producer))
        }
    }

    fun viaCepAPIResponse(cep: Int) {
        viewModelScope.launch {
            when(val response = business.viaCepResponse(cep)) {
                is ResponseAPI.Success -> { viaCepResponseSuccess.postValue(response.data as ViaCep)}
                is ResponseAPI.Error -> {viaCepResponseFailure.postValue(response.message)}
            }
        }
    }
}