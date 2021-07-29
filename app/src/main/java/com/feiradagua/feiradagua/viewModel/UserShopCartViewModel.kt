package com.feiradagua.feiradagua.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.notification.PushNotification
import com.feiradagua.feiradagua.repository.UserShopCartRepository
import kotlinx.coroutines.launch

class UserShopCartViewModel: ViewModel() {
    var orderOk: MutableLiveData<Boolean> = MutableLiveData()
    private val repository by lazy {
        UserShopCartRepository()
    }

    fun sendNewOrder(id: String, order: Order) {
        viewModelScope.launch {
            orderOk.postValue(repository.sendNewOrder(id, order))
        }
    }

    fun sendNotification(notification: PushNotification) {
        viewModelScope.launch {
            repository.sendNotification(notification)
        }
    }
}