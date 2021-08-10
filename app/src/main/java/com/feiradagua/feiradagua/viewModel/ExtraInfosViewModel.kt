package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.business.ExtraInfosBusiness
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.model.cep.ViaCep
import com.feiradagua.feiradagua.repository.ExtraInfosRepository
import kotlinx.coroutines.launch

class ExtraInfosViewModel: ViewModel() {
    val viaCepResponseSuccess: MutableLiveData<ViaCep> = MutableLiveData()
    val viaCepResponseFailure: MutableLiveData<String> = MutableLiveData()
    val userPhoto: MutableLiveData<String?> = MutableLiveData()

    private val repository by lazy {
        ExtraInfosRepository()
    }
    private val business by lazy {
        ExtraInfosBusiness()
    }

    fun setExtraInfosDB(type: Int, adress: String, registered: Registered, phone: String) {
        viewModelScope.launch {
            repository.setExtraInfosDB(type, adress, registered, phone)
        }
    }

    fun setExtrasInfosUser(type: Int, adress: String, registered: Registered, phone: String, deliveryArea: String) {
        viewModelScope.launch {
            repository.setExtraInfosUserDB(type, adress, registered, phone, deliveryArea)
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

    fun getUserPhoto() {
        viewModelScope.launch {
            business.getUserDB()?.let {
                userPhoto.postValue(it)
            }?: run {
                userPhoto.postValue(null)
            }
        }
    }
}