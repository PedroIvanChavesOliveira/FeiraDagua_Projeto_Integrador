package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.model.`class`.notification.PushNotification
import com.feiradagua.feiradagua.repository.OrderDetailsRepository
import kotlinx.coroutines.launch

class OrderDetailsViewModel: ViewModel() {
    var userData: MutableLiveData<User?> = MutableLiveData()
    var updateOk: MutableLiveData<Boolean> = MutableLiveData()
    private val repository by lazy {
        OrderDetailsRepository()
    }

    fun getUserDataById(id: String) {
        viewModelScope.launch {
            repository.let {
                userData.postValue(it.getUserDataById(id)?.toObject(User::class.java))
            }
        }
    }

    fun updateOrderConfirmation(id: String, confirmation: Int) {
        viewModelScope.launch {
            updateOk.postValue(repository.updateOrderConfirmation(id, confirmation))
        }
    }

    fun sendNotification(notification: PushNotification) {
        viewModelScope.launch {
            repository.sendNotification(notification)
        }
    }
}