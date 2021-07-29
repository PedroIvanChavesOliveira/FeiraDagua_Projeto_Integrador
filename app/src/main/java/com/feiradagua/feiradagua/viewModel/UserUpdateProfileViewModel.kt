package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.api.ResponseAPI
import com.feiradagua.feiradagua.business.ProducerUpdateProfileBusiness
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.model.cep.ViaCep
import com.feiradagua.feiradagua.repository.UserUpdateProfileRepository
import kotlinx.coroutines.launch

class UserUpdateProfileViewModel: ViewModel() {
    var updateUser: MutableLiveData<Boolean> = MutableLiveData()
    val viaCepResponseSuccess: MutableLiveData<ViaCep> = MutableLiveData()
    val viaCepResponseFailure: MutableLiveData<String> = MutableLiveData()
    private val repository by lazy {
        UserUpdateProfileRepository()
    }

    private val business by lazy {
        ProducerUpdateProfileBusiness()
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            updateUser.postValue(repository.updateUser(user))
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